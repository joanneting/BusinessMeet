package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.adapter.EventAddParticipantRecyclerViewAdapter;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class EventAddParticipantActivity extends AppCompatActivity implements EventAddParticipantRecyclerViewAdapter.ClickListener {

    private RecyclerView recyclerViewEventAddParticipant;
    private EventAddParticipantRecyclerViewAdapter eventAddParticipantRecyclerViewAdapter;
    private final List<ActivityInviteBean> activityInviteBeanList = new ArrayList<>();
    private List<ActivityInviteBean> inviteList = new ArrayList<>();
    private EditText searchView;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_participant);

        recyclerViewEventAddParticipant = findViewById(R.id.search_participant_ResultView);

        eventAddParticipantRecyclerViewAdapter();
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(this));
        AsyncTaskHelper.execute(() -> FriendServiceImpl.searchInviteList(friendBean), friendBeanList -> {
            for (FriendBean searchBean : friendBeanList) {
                ActivityInviteBean activityInviteBean = new ActivityInviteBean();
                activityInviteBean.setUserId(searchBean.getFriendId());
                activityInviteBean.setUserName(searchBean.getFriendName());
                activityInviteBean.setAvatar(searchBean.getFriendAvatar());
                if (inviteList != null && inviteList.size() > 0) {
                    for (ActivityInviteBean inviteBean : inviteList) {
                        if (inviteBean.getUserId().equals(searchBean.getFriendId())) {
                            activityInviteBean.setInvite(inviteBean.isInvite());
                            activityInviteBeanList.remove(inviteBean);
                            break;
                        } else {
                            activityInviteBean.setInvite(false);

                        }
                    }
                } else {
                    activityInviteBean.setInvite(false);
                }

                eventAddParticipantRecyclerViewAdapter.dataInsert(activityInviteBean);
            }
        });
        searchView = findViewById(R.id.participantSearch_searchbar);
        searchView.addTextChangedListener(textWatcher);
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(addParticipant);
        inviteList = ActivityInviteBean.inviteBean;
    }

    private void eventAddParticipantRecyclerViewAdapter() {
        recyclerViewEventAddParticipant.setLayoutManager(new LinearLayoutManager(this));
        eventAddParticipantRecyclerViewAdapter = new EventAddParticipantRecyclerViewAdapter(this, activityInviteBeanList);
        recyclerViewEventAddParticipant.setAdapter(eventAddParticipantRecyclerViewAdapter);
        eventAddParticipantRecyclerViewAdapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewEventAddParticipant.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewEventAddParticipant.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClick(View view, int position) {
        ActivityInviteBean activityInviteBean = eventAddParticipantRecyclerViewAdapter.getActivityInviteBean(position);
        activityInviteBean.setInvite(!activityInviteBean.isInvite());
        eventAddParticipantRecyclerViewAdapter.dataUpdate(activityInviteBean, position);
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

            setResult(RequestCode.REQUEST_ADD_PARTICIPANT, intent);
            System.out.println("inviteList.size() = " + resultInviteList.size());
            finish();
        }
    };
}
