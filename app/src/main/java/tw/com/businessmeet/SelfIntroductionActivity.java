package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import q.rorbin.badgeview.QBadgeView;
import tw.com.businessmeet.adapter.ProfileTimelineRecyclerViewAdapter;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class SelfIntroductionActivity extends AppCompatActivity implements ProfileTimelineRecyclerViewAdapter.ClickListener {
    private TextView userName, userId;
    private ImageView avatar;
    private ImageButton goProfile;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper;
    private BottomNavigationView menu;
    private final NotificationService notificationService = null;
    private Toolbar toolbar;
    private RecyclerView recyclerViewProfileTimeline;
    private ProfileTimelineRecyclerViewAdapter profileTimelineRecyclerViewAdapter;
    private final List<TimelineBean> timelineBeanList = new ArrayList<>();
//    private QBadgeView qBadgeView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_introduction);
        userName = findViewById(R.id.profile_name);
        userId = findViewById(R.id.user_id);
        avatar = findViewById(R.id.edit_person_photo);
        goProfile = findViewById(R.id.goProfile);
        goProfile.setOnClickListener(goProfileClick);
        menu = findViewById(R.id.bottom_navigation);
        recyclerViewProfileTimeline = findViewById(R.id.profile_timeline_view);
//        qBadgeView = new QBadgeView(content);
//        qBadgeView.bindTarget(view);
//        qBadgeView.hide(true);//隐藏Badge
        //toolbar
        toolbar = findViewById(R.id.toolbar);
        //toolbarMenu
        toolbar.inflateMenu(R.menu.toolbarmenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) { //偵測按下去的事件
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.menu_bell:
                        intent.setClass(SelfIntroductionActivity.this, SelfInviteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_add_calendar:
                        Intent intent1 = intent.setClass(SelfIntroductionActivity.this, EventCreateActivity.class);
                        startActivity(intent1);
                }

                return true;
            }

        });
        TimelineBean timelineBean = new TimelineBean();
        timelineBean.setMatchmakerId(DeviceHelper.getUserId(this));
        AsyncTaskHelper.execute(() -> TimelineServiceImpl.searchList(timelineBean), timelineBeans -> {
            for (TimelineBean searchBean : timelineBeans) {
                profileTimelineRecyclerViewAdapter.dataInsert(searchBean);
            }
        });
        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //searchUserInformation();
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        AvatarHelper avatarHelper = new AvatarHelper();
        createRecyclerViewProfileTimeline();
        Cursor result = userInformationDAO.getById(DeviceHelper.getUserId(this));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        result.close();
    }


    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation() {

        Cursor result = userInformationDAO.getById(DeviceHelper.getUserId(this));

        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("name")));
            userId.append(result.getString(result.getColumnIndex("user_id")));
            avatar.setImageBitmap(AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));
        }
        result.close();


    }

    public View.OnClickListener goProfileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToSelfIntroductionActivityPage();
        }
    };

    public void changeToSelfIntroductionActivityPage() {
        Intent intent = new Intent();
        intent.setClass(SelfIntroductionActivity.this, SelfInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", getIntent().getStringExtra("userId"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void createRecyclerViewProfileTimeline() {
        recyclerViewProfileTimeline.setLayoutManager(new LinearLayoutManager(this));
        profileTimelineRecyclerViewAdapter = new ProfileTimelineRecyclerViewAdapter(this, this.timelineBeanList);
        profileTimelineRecyclerViewAdapter.setClickListener(this);
        recyclerViewProfileTimeline.setAdapter(profileTimelineRecyclerViewAdapter);

    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this, EventActivity.class); //改到活動事件內容
        Bundle bundle = new Bundle();
        bundle.putString("timelineNo", profileTimelineRecyclerViewAdapter.getTimelineBean(position).getTimelineNo().toString());
        bundle.putString("page", "self");
        intent.putExtras(bundle);
        startActivity(intent);

    }


    public void getView(){

    }

    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            return true;
                        case R.id.menu_search:
                            startActivity(new Intent(getApplicationContext()
                                    , SearchActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_friends:
                            //menuItem.setIcon(R.drawable.ic_people_blue_24dp);
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
