package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.dao.TimelineDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class EventCrateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView dateStart,dateEnd,timeStart,timeEnd,addColor,addLocation,addParticipant;
    private TextView event, date, tag, addEventParticipant, addEventMemo,addEventLocation;
    private Switch switchDay;
    private BlueToothHelper blueToothHelper;
    public int timerHour,timerMinute;
    private final Calendar calendar = Calendar.getInstance();
    private Context context;
    private TimelineServiceImpl timelineService = new TimelineServiceImpl();
    private TimelineDAO timelineDAO;
    private AsyncTasKHelper.OnResponseListener<TimelineBean,TimelineBean> addEvent = new AsyncTasKHelper.OnResponseListener<TimelineBean, TimelineBean>() {
        @Override
        public Call<ResponseBody<TimelineBean>> request(TimelineBean... timelineBeans) {
            return timelineService.add(timelineBeans[0]);
        }

        @Override
        public void onSuccess(TimelineBean timelineBean) {
            timelineService.add(timelineBean);

        }

        @Override
        public void onFail(int status, String message) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        context = this;
        DBHelper dbHelper = new DBHelper(this);
        timelineDAO = new TimelineDAO(dbHelper);
        //Event = (TextView) findViewById(R.);
        addEventLocation = findViewById(R.id.add_event_location);
        switchDay = findViewById(R.id.switch_day);
        dateStart = findViewById(R.id.date_start);
        dateEnd = findViewById(R.id.date_end);
        timeStart = findViewById(R.id.time_start);
        timeEnd = findViewById(R.id.time_end);
        blueToothHelper = new BlueToothHelper(this);
        addEventParticipant = findViewById(R.id.add_event_participant);
        addEventMemo = findViewById(R.id.add_event_memo);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.event_create_toolbar);
        toolbar.inflateMenu(R.menu.event_create_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //do back
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_addevent:
                        if(dateStart.getText() != null && dateStart.getText().equals("")){
                            Toast.makeText(context,"請選擇開始日期",Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(dateEnd.getText() != null && dateEnd.getText().equals("")){
                            Toast.makeText(context,"請選擇結束日期",Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(timeStart.getText() != null && timeStart.getText().equals("")){
                            Toast.makeText(context,"請選擇開始時間",Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(timeEnd.getText() != null && timeEnd.getText().equals("")){
                            Toast.makeText(context,"請選擇結束時間",Toast.LENGTH_LONG).show();
                            break;
                        }

                        TimelineBean timelineBean = new TimelineBean();
                        timelineBean.setFriendId(getIntent().getExtras().getString("friendId"));
                        timelineBean.setMatchmakerId(blueToothHelper.getUserId());
                        timelineBean.setStartDate(dateStart+" "+timeStart);
                        timelineBean.setEndDate(dateEnd+" "+timeEnd);
                        timelineBean.setPlace(addEventLocation.getText().toString());
                        timelineBean.setRemark(addEventMemo.getText().toString());
                        AsyncTasKHelper.execute(addEvent,timelineBean);
                }
                return false;
            }

        });



        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventCrateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        dateStart.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.updateDate(year,month,day);
                datePickerDialog.show();
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventCrateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        dateEnd.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.updateDate(year,month,day);
                datePickerDialog.show();
            }
        });

        //Add Color
        addColor = (TextView) findViewById(R.id.add_color);
        addColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EventColorSelectActivity.class);
                startActivity(i);
//                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(CrateEventActivity.this);
//                materialAlertDialogBuilder.setView(R.layout.event_color_select);
            }
        });

        //Add Location
        addLocation = (TextView) findViewById(R.id.add_event_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EventAddLocationActivity.class);
                startActivity(i);
            }
        });

        //Add Participant
        addParticipant = (TextView) findViewById(R.id.add_event_participant);
        addParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EventAddParticipantActivity.class);
                startActivity(i);
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
//                dateStart.setText(materialDatePicker.getHeaderText());
//            }
//        });

//        //Date_End
//        mDatePickerBtnEnd = findViewById(R.id.event_time_select_end);
//        mdateEnd = findViewById(R.id.date_end);
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
//                mdateEnd.setText(materialDatePicker2.getHeaderText());
//            }
//        });



        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventCrateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                timeStart.setText(DateFormat.format("aa hh:mm",calendar));
                            }
                        },12,0,false

                );
                //Displayed previous selected time
                timePickerDialog.updateTime(timerHour,timerMinute);
                //Show dialog
                timePickerDialog.show();

            }
        });



        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventCrateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                timeEnd.setText(DateFormat.format("aa hh:mm",calendar));
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
