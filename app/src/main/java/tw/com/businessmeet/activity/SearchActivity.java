package tw.com.businessmeet.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.adapter.MatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.adapter.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.device.ActionListenerImpl;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.DeviceFinderCompat;
import tw.com.businessmeet.device.MatchListener;
import tw.com.businessmeet.device.actionhandler.supplier.ForegroundActionHandlerSupplier;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class SearchActivity extends AppCompatActivity implements MatchedDeviceRecyclerViewAdapter.SearchClickListener, UnmatchedDeviceRecyclerViewAdapter.MatchedClickListener {
    private DBHelper DH = null;
    private UserInformationDAO userInformationDAO;
    private RecyclerView recyclerViewMatched, recyclerViewUnmatched;
    private MatchedDeviceRecyclerViewAdapter matchedRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedRecyclerViewAdapter;
    private List<UserInformationBean> matchedList = new ArrayList<>();
    private List<UserInformationBean> unmatchedList = new ArrayList<>();
    private TextView search_title;
    private FriendDAO friendDAO;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        openDB();

        recyclerViewMatched = findViewById(R.id.matched);
        recyclerViewUnmatched = findViewById(R.id.unmatched);
        createRecyclerViewUnmatched();
        createRecyclerViewMatched();
        TextView search_title = findViewById(R.id.search_title);
        DeviceFinder foregroundDeviceFinder = DeviceFinderCompat.getForegroundFinder(this);
        foregroundDeviceFinder.find(new ForegroundActionHandlerSupplier(this, new MatchListener() {
            @Override
            public void onMatched(UserInformationBean userInformationBean) {
                matchedRecyclerViewAdapter.dataInsert(userInformationBean);
            }

            @Override
            public void onUnmatched(UserInformationBean userInformationBean) {
                unmatchedRecyclerViewAdapter.dataInsert(userInformationBean);
            }
        }), new ActionListenerImpl(this));

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像

        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        UserInformationBean ufb = new UserInformationBean();
        ufb.setUserId(DeviceHelper.getUserId(this));
        Cursor result = userInformationDAO.searchAll(ufb);

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        result.close();
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        friendDAO = new FriendDAO(DH);
    }

    private void createRecyclerViewMatched() {
        recyclerViewMatched.setLayoutManager(new LinearLayoutManager(this));
        matchedRecyclerViewAdapter = new MatchedDeviceRecyclerViewAdapter(this, this.matchedList);
        matchedRecyclerViewAdapter.setClickListener(this);
        recyclerViewMatched.setAdapter(matchedRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMatched.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMatched.addItemDecoration(dividerItemDecoration);
    }

    private void createRecyclerViewUnmatched() {
        recyclerViewUnmatched.setLayoutManager(new LinearLayoutManager(this));
        unmatchedRecyclerViewAdapter = new UnmatchedDeviceRecyclerViewAdapter(this, this.unmatchedList);
        unmatchedRecyclerViewAdapter.setClickListener(this);
        recyclerViewUnmatched.setAdapter(unmatchedRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewUnmatched.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewUnmatched.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onSearchClick(View view, int position) {
        BluetoothHelper.cancelDiscovery();
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", matchedRecyclerViewAdapter.getUserInformation(position).getUserId());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMatchedClick(View view, int position) {
        UserInformationBean userInformationBean = unmatchedRecyclerViewAdapter.getUserInformation(position);
        FriendBean friendBean = new FriendBean();
        friendBean.setFriendId(userInformationBean.getUserId());
        friendBean.setMatchmakerId(DeviceHelper.getUserId(this));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.add(friendBean), friendDAO::add);
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, FriendSearchActivity.class);
        startActivity(intent);
        finish();
    }


    //button_nav Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override

                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            BluetoothHelper.cancelDiscovery();
                            startActivity(new Intent(getApplicationContext()
                                    , SelfIntroductionActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_search:
                            return true;
                        case R.id.menu_friends:
                            BluetoothHelper.cancelDiscovery();
                            //menuItem.setIcon(R.drawable.ic_people_black_24dp);
                            startActivity(new Intent(getApplicationContext()
                                    , FriendSearchActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });

    @Override
    public void onBackPressed() {
        return;
    }
}
