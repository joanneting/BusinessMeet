package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CrateEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView DateStart,DateEnd,TimerStart,TimerEnd,AddColor;
    private TextView Event, Date, Tag, Participant, EventMemo;
    public int timerHour,timerMinute;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        //Event = (TextView) findViewById(R.);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.event_create_toolbar);
        toolbar.inflateMenu(R.menu.event_create_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do back
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) { //偵測按下去的事件
                return false;
            }

        });


        //Date_Start
        DateStart = findViewById(R.id.date_start);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CrateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        DateStart.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.updateDate(year,month,day);
                datePickerDialog.show();
            }
        });
        //Date_End
        DateEnd = findViewById(R.id.date_end);
        DateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CrateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        DateEnd.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.updateDate(year,month,day);
                datePickerDialog.show();
            }
        });

        //add_color
        AddColor = (TextView) findViewById(R.id.add_color);
        AddColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ColorSelectActivity.class);
                startActivity(i);
//                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(CrateEventActivity.this);
//                materialAlertDialogBuilder.setView(R.layout.event_color_select);
            }
        });


//        //MaterialDatePicker
//        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
//        MaterialDatePicker materialDatePicker = builder.build();
//        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
//
//            }
//        });
//
//        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(Object selection) {
//                DateStart.setText(materialDatePicker.getHeaderText());
//            }
//        });

//        //Date_End
//        mDatePickerBtnEnd = findViewById(R.id.event_time_select_end);
//        mDateEnd = findViewById(R.id.date_end);
//        MaterialDatePicker.Builder builder2 = MaterialDatePicker.Builder.datePicker();
//        MaterialDatePicker materialDatePicker2 = builder2.build();
//        mDatePickerBtnEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                materialDatePicker2.show(getSupportFragmentManager(), "DATE_PICKER");
//            }
//        });
//        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(Object selection) {
//                mDateEnd.setText(materialDatePicker2.getHeaderText());
//            }
//        });


        //Timer_Start
        TimerStart = findViewById(R.id.time_start);
        TimerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CrateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                TimerStart.setText(DateFormat.format("aa hh:mm",calendar));
                            }
                        },12,0,false

                );
                //Displayed previous selected time
                timePickerDialog.updateTime(timerHour,timerMinute);
                //Show dialog
                timePickerDialog.show();

            }
        });


        //Timer_End
        TimerEnd = findViewById(R.id.time_end);
        TimerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CrateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                TimerEnd.setText(DateFormat.format("aa hh:mm",calendar));
                            }
                        },12,0,false

                );
                //Displayed previous selected time
                timePickerDialog.updateTime(timerHour,timerMinute);
                //Show dialog
                timePickerDialog.show();

            }
        });

    }
}
