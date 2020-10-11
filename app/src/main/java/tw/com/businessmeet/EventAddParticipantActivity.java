package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import tw.com.businessmeet.adapter.EventAddParticipantRecyclerViewAdapter;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class EventAddParticipantActivity extends AppCompatActivity implements EventAddParticipantRecyclerViewAdapter.ClickListener  {

    private RecyclerView recyclerViewEventAddParticipant;
    private Activity activity = this;
    private EventAddParticipantRecyclerViewAdapter eventAddParticipantRecyclerViewAdapter;
    private List<ActivityInviteBean> activityInviteBeanList  = new ArrayList<>();
    List<ActivityInviteBean> inviteList = new ArrayList<>();
    private FriendServiceImpl friendService = new FriendServiceImpl();
    private EditText searchView ;
    private Button confirmButton;
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
                if(inviteList != null && inviteList.size() > 0) {
                    for (ActivityInviteBean inviteBean : inviteList) {
                        if (inviteBean.getUserId().equals(friendBean.getFriendId())) {
                            activityInviteBean.setInvite(inviteBean.isInvite());
                            activityInviteBeanList.remove(inviteBean);
                            break;
                        } else {
                            activityInviteBean.setInvite(false);

                        }
                    }
                }else{
                    activityInviteBean.setInvite(false);
                }

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

        recyclerViewEventAddParticipant = findViewById(R.id.search_participant_ResultView);

        eventAddParticipantRecyclerViewAdapter();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        AsyncTasKHelper.execute(searchFriendResponseListener,friendBean);
        searchView = (EditText) findViewById(R.id.participantSearch_searchbar);
        searchView.addTextChangedListener(textWatcher);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(addParticipant);
        inviteList= ActivityInviteBean.inviteBean ;
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

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            eventAddParticipantRecyclerViewAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public View.OnClickListener addParticipant = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<ActivityInviteBean> resultInviteList = new ArrayList<>(eventAddParticipantRecyclerViewAdapter.getInviteList());
           Intent intent = new Intent();

           ActivityInviteBean.inviteBean = resultInviteList;

            activity.setResult(RequestCode.REQUEST_ADD_PARTICIPANT,intent);
            System.out.println("inviteList.size() = " + resultInviteList.size());
            activity.finish();
        }
    };
}
