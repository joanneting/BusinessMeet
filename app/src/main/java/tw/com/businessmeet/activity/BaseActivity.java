package tw.com.businessmeet.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
