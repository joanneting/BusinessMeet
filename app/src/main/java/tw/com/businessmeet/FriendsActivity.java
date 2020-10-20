package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.adapter.FriendsRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class FriendsActivity extends AppCompatActivity implements FriendsRecyclerViewAdapter.ClickListener {
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private TextView searchbar;
    private RecyclerView recyclerViewFriends;
    private BluetoothHelper bluetoothHelper;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        recyclerViewFriends = findViewById(R.id.friendsView);
        searchbar = findViewById(R.id.event_searchbar);
        searchbar.setOnClickListener(searchbarClick);
        //bottomNavigationView
        //Initialize And Assign Variable
        openDB();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.e("result", String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        createRecyclerViewFriends();
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(this, userInformationDAO));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.search(friendBean), friendBeanList -> {
            Log.e("FriendBean", "success");
            if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {
                for (FriendBean searchBean : friendBeanList) {
                    AsyncTaskHelper.execute(
                            () -> UserInformationServiceImpl.getById(searchBean.getFriendId()),
                            friendsRecyclerViewAdapter::dataInsert
                    );
                    Log.e("FriendBean", String.valueOf(searchBean));
                }
            }
        });
    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    private void createRecyclerViewFriends() {
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(this, this.userInformationBeanList);
        friendsRecyclerViewAdapter.setClickListener(this);
        recyclerViewFriends.setAdapter(friendsRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewFriends.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFriends.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(friendsRecyclerViewAdapter.getItemCount()));
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this, FriendsTimelineActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("friendId", friendsRecyclerViewAdapter.getUserInformation(position).getUserId());
        intent.putExtras(bundle);
        startActivity(intent);

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem switchButton = menu.findItem(R.id.menu_friends);
        boolean searchScriptDisplayed = false;
        if(searchScriptDisplayed){
            switchButton.setIcon(R.drawable.ic_people_blue_24dp);
        }else{
            switchButton.setIcon(R.drawable.ic_people_outline_blue_24dp);
        }
        return super.onPrepareOptionsMenu(menu);

    }*/
    }

    public View.OnClickListener searchbarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsSearchActivityPage();
        }
    };

    public void changeToFriendsSearchActivityPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsActivity.this, FriendsSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", DeviceHelper.getUserId(this));
        bundle.putString("blueToothAddress", getIntent().getStringExtra("blueToothAddress"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            startActivity(new Intent(getApplicationContext()
                                    , SelfIntroductionActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_search:
                            startActivity(new Intent(getApplicationContext()
                                    , SearchActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_friends:
                            return true;
                    }
                    return false;
                }
            });


}