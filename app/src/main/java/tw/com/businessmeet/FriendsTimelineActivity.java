package tw.com.businessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.adapter.FriendsTimelineRecyclerViewAdapter;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FriendsTimelineActivity extends AppCompatActivity implements FriendsTimelineRecyclerViewAdapter.ClickListener {
    private TextView userName, company, position, email, tel, memo;
    private Button editButton;
    private ImageButton goProfile;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private BlueToothHelper blueToothHelper;
    private FriendDAO matchedDAO;
    private FriendBean matchedBean = new FriendBean();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private TimelineServiceImpl timelineService = new TimelineServiceImpl();
    private FriendServiceImpl matchedService = new FriendServiceImpl();
    private Toolbar toolbar;
    private RecyclerView recyclerViewFriendsTimeline;
    private FriendsTimelineRecyclerViewAdapter friendsTimelineRecyclerViewAdapter;
    private List<TimelineBean> timelineBeanList = new ArrayList<>();
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> userInfoResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {


        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... userId) {
            return userInformationService.getById(userId[0]);
        }

        @Override
        public void onSuccess(UserInformationBean userInformationBean) {
            userName.append(userInformationBean.getName());
            position.append(userInformationBean.getProfession());
            avatar.setImageBitmap(avatarHelper.getImageResource(userInformationBean.getAvatar()));
        }

        @Override
        public void onFail(int status,String message) {
        }
    };


    private AsyncTasKHelper.OnResponseListener<TimelineBean,List<TimelineBean>> searchTimelineResponseListener =
            new AsyncTasKHelper.OnResponseListener<TimelineBean,List<TimelineBean>>() {
                @Override
                public Call<ResponseBody<List<TimelineBean>>> request(TimelineBean... timelineBeans) {

                    return timelineService.searchList(timelineBeans[0]);
                }

                @Override
                public void onSuccess(List<TimelineBean> timelineBeanList) {
                    for (TimelineBean timelineBean:timelineBeanList){
                        friendsTimelineRecyclerViewAdapter.dataInsert(timelineBean);
                    }
                }

                @Override
                public void onFail(int status, String message) {

                }
            };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_timeline);
        String friendId = getIntent().getStringExtra("friendId");
        AsyncTasKHelper.execute(userInfoResponseListener, friendId);
        matchedBean.setFriendId(friendId);
        blueToothHelper = new BlueToothHelper(this);
        matchedBean.setMatchmakerId(blueToothHelper.getUserId());
        recyclerViewFriendsTimeline = findViewById(R.id.timeline_view);
        TimelineBean timelineBean = new TimelineBean();
        timelineBean.setMatchmakerId(blueToothHelper.getUserId());
        timelineBean.setFriendId(matchedBean.getFriendId());
        AsyncTasKHelper.execute(searchTimelineResponseListener,timelineBean);
        userName = (TextView) findViewById(R.id.friends_name);
        goProfile = (ImageButton) findViewById(R.id.goProfile);
        goProfile.setOnClickListener(goProfileClick);
        position = (TextView) findViewById(R.id.friends_position);
        avatar = (ImageView) findViewById(R.id.friends_photo);
        avatarHelper = new AvatarHelper();
        Log.d("timephoto", avatarHelper.toString());
        
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbarMenu    
        toolbar.inflateMenu(R.menu.timeline_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios_24px);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do back
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) { //偵測按下去的事件
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.menu_toolbar_search:
                        intent.setClass(FriendsTimelineActivity.this, EventSearch.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_addevent:
                        Intent intent1 = intent.setClass(FriendsTimelineActivity.this, EventCreateActivity.class);
                        startActivity(intent1);
                }

                return true;
            }


        });



        //searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        openDB();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);
        createRecyclerViewFriendsTimeline(); //timelineRecycleView
        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));

        if (getIntent().hasExtra("avatar")) {
            ImageView photo = findViewById(R.id.friends_photo);
            Bitmap profilePhoto = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("avatar"), 0, getIntent().getByteArrayExtra("avatar").length);
            photo.setImageBitmap(profilePhoto);
        }
    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        matchedDAO = new FriendDAO(DH);

    }
    private void createRecyclerViewFriendsTimeline() {
        recyclerViewFriendsTimeline.setLayoutManager(new LinearLayoutManager(this));
        friendsTimelineRecyclerViewAdapter = new FriendsTimelineRecyclerViewAdapter(this, this.timelineBeanList);
        friendsTimelineRecyclerViewAdapter.setClickListener(this);
        recyclerViewFriendsTimeline.setAdapter(friendsTimelineRecyclerViewAdapter);

    }

    public void onClick(View view, int position){
        Intent intent = new Intent();
        intent.setClass(this,EventActivity.class); //改到活動事件內容
        Bundle bundle = new Bundle();
        bundle.putString("timelineNo",friendsTimelineRecyclerViewAdapter.getTimelineBean(position).getTimelineNo().toString());
        String friendId = getIntent().getStringExtra("friendId");
        bundle.putString("friendId",friendId);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    public View.OnClickListener goProfileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsIntroductionActivityPage();
        }
    };

    public void changeToFriendsIntroductionActivityPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsTimelineActivity.this, FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId",getIntent().getStringExtra("friendId"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Perform ItemSelectedListener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
                            startActivity(new Intent(getApplicationContext()
                                    ,FriendsActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });

}
