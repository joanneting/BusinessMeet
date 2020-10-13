package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.adapter.MatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.adapter.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;

public class SearchActivity extends AppCompatActivity implements MatchedDeviceRecyclerViewAdapter.SearchClickListener, UnmatchedDeviceRecyclerViewAdapter.MatchedClickListener {
    private DBHelper DH = null;
    private UserInformationDAO userInformationDAO;
    private BlueToothHelper blueTooth;
    private RecyclerView recyclerViewMatched,recyclerViewUnmatched;
    private MatchedDeviceRecyclerViewAdapter matchedRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedRecyclerViewAdapter;
    private List<UserInformationBean> matchedList = new ArrayList<>();
    private List<UserInformationBean> unmatchedList = new ArrayList<>();
    private FriendServiceImpl matchedApi = new FriendServiceImpl();
    private FriendServiceImpl matchedService = new FriendServiceImpl();
    private TextView search_title;
    private FriendDAO friendDAO;
    private BlueToothHelper blueToothHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            try {
                String[] message = ((String) msg.obj).split(",");
                String myUserId = blueTooth.getUserId();
                String friendId = message[0];

                Toast.makeText(SearchActivity.this, friendId, Toast.LENGTH_LONG).show();
                FriendBean friendBean = new FriendBean();
                Log.d("getblueTooth",blueTooth.getUserId());
                friendBean.setMatchmakerId(myUserId);
                friendBean.setFriendId(friendId);
                AsyncTasKHelper.execute(addResponseListener, friendBean);

                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("friendId",friendId);
                intent.putExtras(bundle);
                startActivity(intent);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };


    private AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean>() {
                @Override
                public Call<ResponseBody<FriendBean>> request(FriendBean... friendBean) {

                    return matchedService.add(friendBean[0]);
                }

                @Override
                public void onSuccess(FriendBean friendBean) {
                    friendDAO.add(friendBean);
                }

                @Override
                public void onFail(int status,String message) {

                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        openDB();

        recyclerViewMatched = findViewById(R.id.matched);
        recyclerViewUnmatched = findViewById(R.id.unmatched);
        createRecyclerViewUnmatched();
        createRecyclerViewMatched();
        TextView search_title =  findViewById(R.id.search_title);
        Log.e("searhTitle",String.valueOf(search_title));
        blueTooth = new BlueToothHelper(this);
        blueTooth.startBuleTooth();
        blueTooth.scanBluth();
        blueTooth.searchBlueTooth(userInformationDAO,matchedRecyclerViewAdapter,unmatchedRecyclerViewAdapter);
        blueTooth.startThread(handler);

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像

        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        AvatarHelper avatarHelper = new AvatarHelper();
        //blueToothHelper.startBuleTooth();
        Log.d("seedmess","ness");
        UserInformationBean ufb = new UserInformationBean();
        //ufb.setMatchmaker(blueToothHelper.getUserId());
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.e("result",String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));



    }

    private void openDB(){
        Log.d("add","openDB");
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
        Log.d("resultMainAdapter", String.valueOf(matchedRecyclerViewAdapter.getItemCount()));
    }
    private void createRecyclerViewUnmatched() {
        recyclerViewUnmatched.setLayoutManager(new LinearLayoutManager(this));
        unmatchedRecyclerViewAdapter = new UnmatchedDeviceRecyclerViewAdapter(this, this.unmatchedList);
        unmatchedRecyclerViewAdapter.setClickListener(this);
        recyclerViewUnmatched.setAdapter(unmatchedRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewUnmatched.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewUnmatched.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(unmatchedRecyclerViewAdapter.getItemCount()));
    }
    @Override
    public void onSearchClick(View view, int position) {

        blueTooth.cancelDiscovery();
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId",matchedRecyclerViewAdapter.getUserInformation(position).getUserId());
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("send","============================");
        //NotificationHelper notificationHelper = new NotificationHelper(this);
        //notificationHelper.sendMessage(address);
    }
    @Override
    public void onMatchedClick(View view, int position) {
        Log.d("results","success");
        Log.d("results",String.valueOf(position));
        blueTooth.cancelDiscovery();
        UserInformationBean userInformationBean = unmatchedRecyclerViewAdapter.getUserInformation(position);
        String address = userInformationBean.getIdentifier();
        String userName = userInformationBean.getName();
        blueTooth.matched(address,userName,addResponseListener, friendDAO);
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId",unmatchedRecyclerViewAdapter.getUserInformation(position).getUserId());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //button_nav Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override

        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_home:
                    blueTooth.cancelDiscovery();
                    startActivity(new Intent(getApplicationContext()
                            ,SelfIntroductionActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menu_search:
                    return true;
                case R.id.menu_friends:
                    blueTooth.cancelDiscovery();
                    //menuItem.setIcon(R.drawable.ic_people_black_24dp);
                    startActivity(new Intent(getApplicationContext()
                            ,FriendsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }
    });

}
