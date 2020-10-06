package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.businessmeet.adapter.EventAddLocationRecyclerViewAdapter;
import tw.com.businessmeet.adapter.EventAddParticipantRecyclerViewAdapter;

import android.os.Bundle;
import android.widget.CheckBox;

public class EventAddParticipantActivity extends AppCompatActivity implements EventAddParticipantRecyclerViewAdapter.ClickListener {

    private RecyclerView recyclerViewEventAddParticipant;
    private EventAddLocationRecyclerViewAdapter eventAddParticipantRecyclerViewAdapter;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_participant);

        //checkbox
        recyclerViewEventAddParticipant = findViewById(R.id.search_participant_ResultView);
        checkBox = (CheckBox) findViewById(R.id.participant_check);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }
        eventAddParticipantRecyclerViewAdapter();
    }

    private void eventAddParticipantRecyclerViewAdapter() {
        recyclerViewEventAddParticipant.setLayoutManager(new LinearLayoutManager(this));
        eventAddParticipantRecyclerViewAdapter = new EventAddLocationRecyclerViewAdapter(this);
        recyclerViewEventAddParticipant.setAdapter(eventAddParticipantRecyclerViewAdapter);

    }
}
