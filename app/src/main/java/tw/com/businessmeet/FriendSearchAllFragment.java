package tw.com.businessmeet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.adapter.FriendsRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class FriendSearchAllFragment extends Fragment implements FriendsRecyclerViewAdapter.ClickListener {
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private TextView searchbar;
    private RecyclerView recyclerViewFriends;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_search_all, container, false);

        recyclerViewFriends = view.findViewById(R.id.friends_edit_profile_memo_recycleView);
        searchbar = view.findViewById(R.id.event_searchbar);
        //bottomNavigationView
        //Initialize And Assign Variable
        openDB();


        createRecyclerViewFriends();
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(getContext(), userInformationDAO));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.search(friendBean), friendBeanList -> {
            Log.e("FriendBean", "success");
            if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {
                for (FriendBean searchBean : friendBeanList) {
                    AsyncTaskHelper.execute(
                            () -> UserInformationServiceImpl.getById(searchBean.getFriendId()),
                            friendsRecyclerViewAdapter::dataInsert
                    );
                    Log.e("FriendBean", String.valueOf(searchBean));
                }
            }
        });
        return view;
    }

    private void openDB() {
        Log.d("add", "openDB");
        System.out.println("getActivity() = " + getActivity());
        DH = new DBHelper(getActivity());
        System.out.println("DH = " + DH);
        userInformationDAO = new UserInformationDAO(DH);
    }

    private void createRecyclerViewFriends() {
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(getActivity(), this.userInformationBeanList);
        friendsRecyclerViewAdapter.setClickListener(this::onClick);
        recyclerViewFriends.setAdapter(friendsRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewFriends.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFriends.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(friendsRecyclerViewAdapter.getItemCount()));
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendsTimelineActivity.class);
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







}
