package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.businessmeet.adapter.EventAddLocationRecyclerViewAdapter;
import tw.com.businessmeet.adapter.FriendsTimelineRecyclerViewAdapter;

import android.os.Bundle;

public class EventAddLocationActivity extends AppCompatActivity implements EventAddLocationRecyclerViewAdapter.ClickListener {
    private RecyclerView  recyclerViewEventAddLocation;
    private EventAddLocationRecyclerViewAdapter eventAddLocationRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_location);

        recyclerViewEventAddLocation = findViewById(R.id.search_location_ResultView);
        createRecyclerViewEventAddLocation();
    }

    private void createRecyclerViewEventAddLocation() {
        recyclerViewEventAddLocation.setLayoutManager(new LinearLayoutManager(this));
        eventAddLocationRecyclerViewAdapter = new EventAddLocationRecyclerViewAdapter(this);
        recyclerViewEventAddLocation.setAdapter(eventAddLocationRecyclerViewAdapter);

    }
}
