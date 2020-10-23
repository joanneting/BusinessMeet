package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;

public class FriendsActivity extends AppCompatActivity implements FriendsRecyclerViewAdapter.ClickListener {
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private TextView friendsToolbarTitle;
    private EditText eventSearchbar;
    private RecyclerView recyclerViewFriends;
    private BluetoothHelper bluetoothHelper;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerViewFriends = findViewById(R.id.friendsView);
        eventSearchbar = findViewById(R.id.event_searchbar);
        eventSearchbar.addTextChangedListener(textWatcher);
        friendsToolbarTitle = findViewById(R.id.friends_toolbar_title);

        //bottomNavigationView
        //Initialize And Assign Variable
        openDB();
        Integer groupNo = Integer.parseInt(getIntent().getStringExtra("groupNo"));
        String groupName = getIntent().getStringExtra("groupName");
        friendsToolbarTitle.setText(groupName);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        ufb.setUserId(DeviceHelper.getUserId(this));
        Cursor result = userInformationDAO.searchAll(ufb);

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        createRecyclerViewFriends();
        AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.searchFriendByGroup(groupNo), friendGroupBeanList -> {
            if (friendGroupBeanList.size() > 0) {
                for (FriendGroupBean friendGroupBean : friendGroupBeanList) {
                    UserInformationBean userInformationBean = new UserInformationBean();
                    FriendBean friendBean = friendGroupBean.getFriendBean();
                    userInformationBean.setAvatar(friendBean.getFriendAvatar());
                    userInformationBean.setProfession(friendBean.getFriendProfession());
                    userInformationBean.setName(friendBean.getFriendName());
                    userInformationBean.setUserId(friendBean.getFriendId());
                    friendsRecyclerViewAdapter.dataInsert(userInformationBean);
                }
            }
        });
    }

    private void openDB() {
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
        intent.setClass(FriendsActivity.this, OpenActivityFriendsSearchActivity.class);
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
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            friendsRecyclerViewAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}