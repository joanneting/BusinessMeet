package tw.com.businessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.businessmeet.adapter.FriendProfileListViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName, id, profession, gender, email, tel, remark;
    private Button editButton;
    private ImageView avatar;
    private ListView listView;
    private String friendId;
    private Integer friendNo;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private BlueToothHelper blueToothHelper;
    private FriendDAO friendDAO;
    private FriendBean friendBean = new FriendBean();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private FriendServiceImpl matchedService = new FriendServiceImpl();
    private ArrayList<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();

    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> userInfoResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... userId) {
            return userInformationService.getById(userId[0]);
        }

        @Override
        public void onSuccess(UserInformationBean userInformationBean) {
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
            avatar.setImageBitmap(avatarHelper.getImageResource(userInformationBean.getAvatar()));
        }

        @Override
        public void onFail(int status, String message) {
        }
    };

    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> friendsMemoResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return matchedService.search(friendBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
            System.out.println("friendBeanList.size() = " + friendBeanList.size());
            System.out.println("friendBeanList.get(0) = " + friendBeanList.get(0));
            System.out.println("friendBeanList.get(0).getRemark() = " + friendBeanList.get(0).getRemark());
            if(friendBeanList.get(0).getRemark() != null){
                System.out.println("remark - " + friendBeanList.get(0).getRemark());
                    remark.append(friendBeanList.get(0).getRemark());
            }

            friendNo = friendBeanList.get(0).getFriendNo();
            FriendCustomizationBean fcb = new FriendCustomizationBean();
            fcb.setFriendNo(friendNo);
            System.out.println("friendNo = " + friendNo);
            AsyncTasKHelper.execute(searchResponseListener, fcb);
        }

        @Override
        public void onFail(int status, String message) {

        }
    };

    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, List<FriendCustomizationBean>> searchResponseListener = new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, List<FriendCustomizationBean>>() {

        @Override
        public Call<ResponseBody<List<FriendCustomizationBean>>> request(FriendCustomizationBean... friendCustomizationBeans) {
            return friendCustomizationServiceImpl.search(friendCustomizationBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendCustomizationBean> friendCustomizationBeans) {
            if (friendCustomizationBeans.size() > 1 || (friendCustomizationBeans.size() == 1 && (friendCustomizationBeans.get(0).getCreateDate() != null && !friendCustomizationBeans.get(0).equals("")))) {
                for (int i = 0; i < friendCustomizationBeans.size(); i++) {
                    friendCustomizationBeanList.add(friendCustomizationBeans.get(i));
                }
                FriendProfileListViewAdapter friendProfileListViewAdapter = new FriendProfileListViewAdapter(  FriendsIntroductionActivity.this, friendCustomizationBeanList);
                listView.setAdapter(friendProfileListViewAdapter);
                setListViewHeight(listView);
            }
        }

        @Override
        public void onFail(int status, String message) {
        }
    };

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
        remark = (TextView) findViewById(R.id.firends_profile_information_remark);
        friendId = getIntent().getStringExtra("friendId");
        AsyncTasKHelper.execute(userInfoResponseListener, friendId);
        friendBean.setFriendId(friendId);
        blueToothHelper = new BlueToothHelper(this);
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        AsyncTasKHelper.execute(friendsMemoResponseListener, friendBean);

        userName = (TextView) findViewById(R.id.friends_profile_information_name);
        id = (TextView) findViewById(R.id.friends_profile_information_id);
        profession = (TextView) findViewById(R.id.friends_profile_information_occupation);
        gender = (TextView) findViewById(R.id.friends_profile_information_gender);
        email = (TextView) findViewById(R.id.friends_profile_information_email);
        tel = (TextView) findViewById(R.id.friends_profile_information_phone);
        avatar = (ImageView) findViewById(R.id.friends_profile_information_photo);
        avatarHelper = new AvatarHelper();
        editButton = (Button) findViewById(R.id.friends_profile_information_edit);
        editButton.setOnClickListener(editMemoButton);
        listView = (ListView) findViewById(R.id.friends_profile_information_memo);

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
        friendDAO = new FriendDAO(DH);

    }

    public View.OnClickListener editMemoButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsEditIntroductionPage();
        }
    };

    public void changeToFriendsEditIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsIntroductionActivity.this, EditFriendsProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", getIntent().getStringExtra("friendId"));
        bundle.putInt("friendNo", friendNo);
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
                                    , FriendsActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });
}
