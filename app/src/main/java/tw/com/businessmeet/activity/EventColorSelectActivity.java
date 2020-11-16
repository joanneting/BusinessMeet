package tw.com.businessmeet.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.R;

public class EventColorSelectActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_color_select);

        //Pop
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;


        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


//        //radioButton
//        //radioGroup = (RadioGroup) findViewById(R.id.mRadioGroup); //create the RadioGroup
//        radioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        radioButton = new RadioButton(radioButton.getContext());
//        //radioButton.setText(inputDetailsList.get(i).get("textcontent"));
//        radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        if (ViewCompat.getLayoutDirection(radioButton)== ViewCompat.LAYOUT_DIRECTION_LTR){
//            ViewCompat.setLayoutDirection(radioButton,ViewCompat.LAYOUT_DIRECTION_RTL);
//        }else{
//            ViewCompat.setLayoutDirection(radioButton,ViewCompat.LAYOUT_DIRECTION_LTR);
//        }
//        radioGroup.addView(radioButton);
    }

}
