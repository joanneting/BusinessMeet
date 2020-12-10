package tw.com.businessmeet.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;

public class FriendsFilterActivity extends AppCompatActivity {
    private tw.com.businessmeet.dao.UserInformationDAO userInformationDAO;
    private DBHelper DH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_filter);
        openDB();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
        result.close();
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new tw.com.businessmeet.dao.UserInformationDAO(DH);
        //matchedDAO = new tw.com.bussinessmeet.dao.MatchedDAO(DH);
    }

    public View.OnClickListener backFriendsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsActivityPage();
        }
    };

    public void changeToFriendsActivityPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsFilterActivity.this, FriendsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress", getIntent().getStringExtra("blueToothAddress"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

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

