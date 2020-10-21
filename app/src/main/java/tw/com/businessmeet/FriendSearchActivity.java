package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;

public class FriendSearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    //tab
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FriendSearchAllFragment friendSearchAllFragment;
    private FriendSearchGroupFragment friendSearchGroupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search);
        openDB();
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.search_friend_topAppBar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();        //tab
        AvatarHelper avatarHelper = new AvatarHelper();

        Cursor result = userInformationDAO.getById(DeviceHelper.getUserId(this));


        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.search_friend_tabs);

        friendSearchAllFragment = new FriendSearchAllFragment();
        friendSearchGroupFragment = new FriendSearchGroupFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(friendSearchAllFragment, "好友");
        viewPagerAdapter.addFragment(friendSearchGroupFragment, "群組");
        viewPager.setAdapter(viewPagerAdapter);

    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    //tab
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentsTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
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
                            //menuItem.setIcon(R.drawable.ic_people_blue_24dp);

                            return true;
                    }
                    return false;
                }


            });

}