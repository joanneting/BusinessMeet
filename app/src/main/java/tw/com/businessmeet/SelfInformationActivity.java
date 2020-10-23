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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;

public class SelfInformationActivity extends AppCompatActivity {
    private TextView userName, profession, gender, email, tel;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private BottomNavigationView menu;
    private final NotificationService notificationService = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_information);
        userName = findViewById(R.id.profile_name);
        profession = findViewById(R.id.profile_profession);
        gender = findViewById(R.id.profile_gender);
        email = findViewById(R.id.profile_mail);
        tel = findViewById(R.id.profile_tel);
        avatar = findViewById(R.id.edit_person_photo);
        editButton = findViewById(R.id.editPersonalProfileButton);
        editButton.setOnClickListener(editButtonClick);
        menu = findViewById(R.id.bottom_navigation);
        //this.personal = personal;
        //toolbar
        toolbar = findViewById(R.id.toolbar);
        //toolbarMenu
        toolbar.inflateMenu(R.menu.toolbarmenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_16dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) { //偵測按下去的事件
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.menu_bell:
                        intent.setClass(SelfInformationActivity.this, SelfInviteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_add_calendar:
                        Intent intent1 = intent.setClass(SelfInformationActivity.this, EventCreateActivity.class);
                        startActivity(intent1);
                }

                return true;
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
        BluetoothHelper.startBluetooth(this);
        Log.d("seedmess", "ness");
        Cursor result = userInformationDAO.getById(DeviceHelper.getUserId(this, userInformationDAO));
        Log.e("result", String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));

    }


    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation() {

        Cursor result = userInformationDAO.getById(DeviceHelper.getUserId(this, userInformationDAO));
        Log.d("result", String.valueOf(result.getColumnCount()));

        for (int i = 0; i < result.getColumnCount(); i++) {
            Log.d("result", result.getColumnName(i));
        }


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("name")));
            gender.append(result.getString(result.getColumnIndex("gender")));
            profession.append(result.getString(result.getColumnIndex("profession")));
            email.append(result.getString(result.getColumnIndex("mail")));
            tel.append(result.getString(result.getColumnIndex("tel")));
            avatar.setImageBitmap(AvatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

        }
        result.close();


    }

    public View.OnClickListener editButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToEditIntroductionPage();
        }
    };

    public void changeToEditIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(SelfInformationActivity.this, EditIntroductionActivity.class);
        startActivity(intent);
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


}
