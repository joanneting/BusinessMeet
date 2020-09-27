package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import tw.com.businessmeet.adapter.EventAddLocationRecyclerViewAdapter;

import android.os.Bundle;

public class EventAddLocationActivity extends AppCompatActivity implements EventAddLocationRecyclerViewAdapter.ClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_location);
    }
}
