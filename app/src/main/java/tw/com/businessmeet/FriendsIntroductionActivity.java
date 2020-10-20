package tw.com.businessmeet;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tw.com.businessmeet.adapter.FriendProfileListViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName, id, profession, gender, email, tel, remark, title;
    private Button editButton, deleteButton;
    private ImageView avatar;
    private ListView listView;
    private String friendId, content;
    private Integer friendNo;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private final FriendBean friendBean = new FriendBean();
    private final ArrayList<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();

    private static void setListViewHeight(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter titleAdapter = listView.getAdapter();
        if (titleAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < titleAdapter.getCount(); i++) {
            View listItem = titleAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (titleAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile);
        openDB();
        remark = findViewById(R.id.firends_profile_information_remark);
        friendId = getIntent().getStringExtra("friendId");
        AsyncTaskHelper.execute(() -> UserInformationServiceImpl.getById(friendId), userInformationBean -> {
            if (userInformationBean == null) {
                Cursor cursor = userInformationDAO.getById(friendId);
                userInformationBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                userInformationBean.setProfession(cursor.getString(cursor.getColumnIndex("profession")));
                userInformationBean.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                userInformationBean.setMail(cursor.getString(cursor.getColumnIndex("mail")));
                userInformationBean.setTel(cursor.getString(cursor.getColumnIndex("tel")));
                userInformationBean.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            }
            id.append(userInformationBean.getUserId());
            userName.append(userInformationBean.getName());
            profession.append(userInformationBean.getProfession());
            gender.append(userInformationBean.getGender());
            email.append(userInformationBean.getMail());
            tel.append(userInformationBean.getTel());
            avatar.setImageBitmap(AvatarHelper.getImageResource(userInformationBean.getAvatar()));
        });
        friendBean.setFriendId(friendId);
        friendBean.setMatchmakerId(DeviceHelper.getUserId(this, userInformationDAO));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.search(friendBean), friendBeanList -> {
            if (friendBeanList.get(0).getRemark() != null) {
                content = friendBeanList.get(0).getRemark();
                remark.append(friendBeanList.get(0).getRemark());
            }

            friendNo = friendBeanList.get(0).getFriendNo();
            FriendCustomizationBean fcb = new FriendCustomizationBean();
            fcb.setFriendNo(friendNo);
            System.out.println("friendNo = " + friendNo);
            AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.search(fcb), friendCustomizationBeans -> {
                if (friendCustomizationBeans.size() > 1 || (friendCustomizationBeans.size() == 1 && (friendCustomizationBeans.get(0).getCreateDate() != null && !friendCustomizationBeans.get(0).equals("")))) {
                    friendCustomizationBeanList.addAll(friendCustomizationBeans);
                    FriendProfileListViewAdapter friendProfileListViewAdapter = new FriendProfileListViewAdapter(FriendsIntroductionActivity.this, friendCustomizationBeanList);
                    listView.setAdapter(friendProfileListViewAdapter);
                    setListViewHeight(listView);
                }
            });
        });

        userName = findViewById(R.id.friends_profile_information_name);
        id = findViewById(R.id.friends_profile_information_id);
        profession = findViewById(R.id.friends_profile_information_occupation);
        gender = findViewById(R.id.friends_profile_information_gender);
        email = findViewById(R.id.friends_profile_information_email);
        tel = findViewById(R.id.friends_profile_information_phone);
        avatar = findViewById(R.id.friends_profile_information_photo);
        editButton = findViewById(R.id.friends_profile_information_edit);
        editButton.setOnClickListener(editMemoButton);
        listView = findViewById(R.id.friends_profile_information_memo);
        deleteButton = findViewById(R.id.friends_profile_information_delete);
        deleteButton.setOnClickListener(deleteListener);
        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));


    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public View.OnClickListener editMemoButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsEditIntroductionPage();
        }
    };
    public View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("friendNo = " + friendNo);
            AsyncTaskHelper.execute(() -> FriendServiceImpl.delete(friendNo), empty -> {
                Intent intent = new Intent();
                intent.setClass(FriendsIntroductionActivity.this, FriendSearchActivity.class);
                startActivity(intent);
            });
        }
    };

    public void changeToFriendsEditIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsIntroductionActivity.this, EditFriendsProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", getIntent().getStringExtra("friendId"));
        bundle.putString("userId", friendBean.getMatchmakerId());
        bundle.putInt("friendNo", friendNo);
        bundle.putString("remark", content);
        bundle.putString("matchmakerId", friendBean.getMatchmakerId());

        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Perform ItemSelectedListener
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
                                    , FriendSearchActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });
}