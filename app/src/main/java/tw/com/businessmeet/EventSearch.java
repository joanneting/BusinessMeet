package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EventSearch extends AppCompatActivity {
    private TextView searchbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_search);
        searchbar = (TextView) findViewById(R.id.event_searchbar);
        searchbar.setOnClickListener(searchbarClick);


    }

    public View.OnClickListener searchbarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsSearchActivityPage();
        }
    };

    public void changeToFriendsSearchActivityPage() {
        Intent intent = new Intent();
//        intent.setClass(FriendsActivity.this, FriendsSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress", getIntent().getStringExtra("blueToothAddress"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
