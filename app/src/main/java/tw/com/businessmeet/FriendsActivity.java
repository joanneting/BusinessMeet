package tw.com.businessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.adapter.FriendsRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;

public class FriendsActivity extends AppCompatActivity implements FriendsRecyclerViewAdapter.ClickListener {
    private Button button;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private TextView searchbar;
    private FriendDAO friendDAO;
    private FriendServiceImpl friendService = new FriendServiceImpl();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private BlueToothHelper blueToothHelper;
    private RecyclerView recyclerViewFriends;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();
    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> searchResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
                @Override
                public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBean) {

                    return friendService.search(friendBean[0]);
                }

                @Override
                public void onSuccess(List<FriendBean> friendBeanList) {
                    Log.e("FriendBean", "success");
                    if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {

                        for (FriendBean friendBean : friendBeanList) {
                            AsyncTasKHelper.execute(getByIdResponseListener, friendBean.getFriendId());
                            Log.e("FriendBean", String.valueOf(friendBean));
                        }
                    }
                }

                @Override
                public void onFail(int status,String message) {
//                    if(status == 500){
//                        Intent intent = new Intent();
//                        intent.setClass(FriendsActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                    }
                }
            };
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> getByIdResponseListener =
            new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(String... userId) {

                    return userInformationService.getById(userId[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {
                    Log.e("FriendBean", "user");
                    friendsRecyclerViewAdapter.dataInsert(userInformationBean);
                }

                @Override
                public void onFail(int status,String message) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        recyclerViewFriends = findViewById(R.id.friendsView);
        searchbar = (TextView) findViewById(R.id.event_searchbar);
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
        blueToothHelper = new BlueToothHelper(this);
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        AsyncTasKHelper.execute(searchResponseListener, friendBean);
    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        friendDAO = new FriendDAO(DH);
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