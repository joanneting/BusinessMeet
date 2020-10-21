package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.adapter.FriendsRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class FriendSearchAllFragment extends Fragment implements FriendsRecyclerViewAdapter.ClickListener {
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private EditText searchbar;
    private RecyclerView recyclerViewFriends;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_search_all, container, false);

        recyclerViewFriends = view.findViewById(R.id.friends_edit_profile_memo_recycleView);
        searchbar = view.findViewById(R.id.friendsSearch_searchbar);
        searchbar.addTextChangedListener(textWatcher);
        //bottomNavigationView
        //Initialize And Assign Variable
        openDB();


        createRecyclerViewFriends();
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(getContext(), userInformationDAO));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.search(friendBean), friendBeanList -> {
            if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {
                for (FriendBean searchBean : friendBeanList) {
                    AsyncTaskHelper.execute(
                            () -> UserInformationServiceImpl.getById(searchBean.getFriendId()),
                            friendsRecyclerViewAdapter::dataInsert
                    );
                }
            }
        });
        return view;
    }

    private void openDB() {
        DH = new DBHelper(getActivity());
        userInformationDAO = new UserInformationDAO(DH);
    }

    private void createRecyclerViewFriends() {
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(getActivity(), this.userInformationBeanList);
        friendsRecyclerViewAdapter.setClickListener(this::onClick);
        recyclerViewFriends.setAdapter(friendsRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewFriends.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFriends.addItemDecoration(dividerItemDecoration);
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
