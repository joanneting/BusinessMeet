package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import tw.com.businessmeet.adapter.EventAddParticipantRecyclerViewAdapter;

import android.os.Bundle;

public class EventAddParticipantActivity extends AppCompatActivity implements EventAddParticipantRecyclerViewAdapter.ClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_participant);
    }
}
