package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.adapter.FriendGroupRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.FriendGroupService;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;

public class FriendSearchGroupFragment extends Fragment implements FriendGroupRecyclerViewAdapter.ClickListener {
    private DBHelper DH = null;
    private TextView searchbar;
    private RecyclerView recyclerViewFriendsGroup;
    private FriendGroupRecyclerViewAdapter friendGroupRecyclerViewAdapter;
    private FriendGroupServiceImpl friendGroupService = new FriendGroupServiceImpl();
    private List<FriendGroupBean> friendGroupBeanList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_search_group, container, false);
        recyclerViewFriendsGroup = view.findViewById(R.id.friends_group_recycleView);
        searchbar = view.findViewById(R.id.friendsSearch_searchbar);
        searchbar.addTextChangedListener(textWatcher);
        createRecyclerViewFriendsGroup();
        AsyncTaskHelper.execute(()-> friendGroupService.searchCount(),friendGroupBeans -> {
            for (FriendGroupBean friendGroupBean : friendGroupBeans) {
                friendGroupRecyclerViewAdapter.dataInsert(friendGroupBean);
            }
        });
        return view;
    }
    private void createRecyclerViewFriendsGroup() {
        recyclerViewFriendsGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendGroupRecyclerViewAdapter = new FriendGroupRecyclerViewAdapter(getActivity(), this.friendGroupBeanList);
        friendGroupRecyclerViewAdapter.setClickListener(this::onClick);
        recyclerViewFriendsGroup.setAdapter(friendGroupRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewFriendsGroup.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFriendsGroup.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(friendGroupRecyclerViewAdapter.getItemCount()));
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(),FriendsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("groupNo",friendGroupRecyclerViewAdapter.getFriendGroupBean(position).getGroupNo().toString());
        bundle.putString("groupName",friendGroupRecyclerViewAdapter.getFriendGroupBean(position).getGroupName());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            friendGroupRecyclerViewAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
