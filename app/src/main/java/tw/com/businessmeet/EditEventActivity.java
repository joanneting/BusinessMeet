package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EditEventActivity extends AppCompatActivity {
    private TextView editEvent,eventDate,switchDay,dateStart,timeStart,dateEnd,timeEnd,eventTag,moreEventTag,editEventLocation,editEventParticipant,editColor,editEventMemo;
    private ImageView clockIcon,tagIcon,locationIcon,participantIcon,addEventColor,memoIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        editEvent = findViewById(R.id.edit_event);
        eventDate = findViewById(R.id.event_date);
        switchDay = findViewById(R.id.switch_day);
        dateStart = findViewById(R.id.date_start);
        timeStart = findViewById(R.id.time_start);
        dateEnd = findViewById(R.id.date_end);
        timeEnd = findViewById(R.id.time_end);
        eventTag = findViewById(R.id.event_tag);
        moreEventTag = findViewById(R.id.more_event_tag);
        editEventLocation = findViewById(R.id.edit_event_location);
        editColor = findViewById(R.id.edit_color);
        editEventMemo = findViewById(R.id.event_tag);
        editEventParticipant = findViewById(R.id.edit_event_participant);
        clockIcon = findViewById(R.id.clock_icon);
        tagIcon = findViewById(R.id.tag_icon);
        locationIcon = findViewById(R.id.location_icon);
        addEventColor = findViewById(R.id.add_event_color);
        memoIcon = findViewById(R.id.memo_icon);

        participantIcon = findViewById(R.id.participant_icon);
        tagIcon = findViewById(R.id.tag_icon);
        Bundle bundle = getIntent().getExtras();
        String action = bundle.getString("action");
        switch (action) {
            case "meet":
                editEvent.setText(bundle.getString("title"));
                eventDate.setText(bundle.getString("eventDate"));
                editEventLocation.setText(bundle.getString("place"));
//                editColor.setText(); //時間軸顏色

                switchDay.setVisibility(View.GONE);
                dateStart.setVisibility(View.GONE);
                timeStart.setVisibility(View.GONE);
                dateEnd.setVisibility(View.GONE);
                timeEnd.setVisibility(View.GONE);
                tagIcon.setVisibility(View.GONE);
                eventTag.setVisibility(View.GONE);
                moreEventTag.setVisibility(View.GONE);
                participantIcon.setVisibility(View.GONE);
                editEventParticipant.setVisibility(View.GONE);
            case "activity" :
                editEvent.setText(bundle.getString("title"));

//                dateStart.setText();

        }
        if(action.equals("meet")){


        }

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_create_toolbar);
        toolbar.inflateMenu(R.menu.event_create_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //do back
            }
        });
    }
}
