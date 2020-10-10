package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import tw.com.businessmeet.adapter.EventAddLocationRecyclerViewAdapter;
import tw.com.businessmeet.adapter.EventAddParticipantRecyclerViewAdapter;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class EventAddParticipantActivity extends AppCompatActivity implements EventAddParticipantRecyclerViewAdapter.ClickListener {

    private RecyclerView recyclerViewEventAddParticipant;
    private EventAddParticipantRecyclerViewAdapter eventAddParticipantRecyclerViewAdapter;
    private List<ActivityInviteBean> activityInviteBeanList  = new ArrayList<>();
    private FriendServiceImpl friendService = new FriendServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendBean,List<FriendBean>> searchFriendResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return friendService.searchInviteAvatar(friendBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
            for (FriendBean friendBean : friendBeanList){
                ActivityInviteBean activityInviteBean = new ActivityInviteBean();
                activityInviteBean.setUserId(friendBean.getFriendId());
                activityInviteBean.setUserName(friendBean.getFriendName());
                activityInviteBean.setAvatar(friendBean.getFriendAvatar());
                activityInviteBean.setInvite(false);
                eventAddParticipantRecyclerViewAdapter.dataInsert(activityInviteBean);
            }
        }

        @Override
        public void onFail(int status, String message) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_participant);

        //checkbox
        recyclerViewEventAddParticipant = findViewById(R.id.search_participant_ResultView);

        eventAddParticipantRecyclerViewAdapter();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        System.out.println("blueToothHelper.getUserId() = " + blueToothHelper.getUserId());
        AsyncTasKHelper.execute(searchFriendResponseListener,friendBean);
    }

    private void eventAddParticipantRecyclerViewAdapter() {
        recyclerViewEventAddParticipant.setLayoutManager(new LinearLayoutManager(this));
        eventAddParticipantRecyclerViewAdapter = new EventAddParticipantRecyclerViewAdapter(this,activityInviteBeanList);
        recyclerViewEventAddParticipant.setAdapter(eventAddParticipantRecyclerViewAdapter);
        eventAddParticipantRecyclerViewAdapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewEventAddParticipant.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewEventAddParticipant.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClick(View view, int position) {
        ActivityInviteBean activityInviteBean = eventAddParticipantRecyclerViewAdapter.getActivityInviteBean(position);
        activityInviteBean.setInvite(!activityInviteBean.isInvite());
        eventAddParticipantRecyclerViewAdapter.dataUpdate(activityInviteBean,position);
//      
    }
}
