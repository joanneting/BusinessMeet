package tw.com.businessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.ActivityLabelBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class EditEventActivity extends AppCompatActivity {
    private TextView editEvent,eventDate,dateStart,timeStart,dateEnd,timeEnd,moreEventTag,editEventParticipant,editColor,editEventMemo;
    private EditText editEventLocation,addActivityLabel;
    private ChipGroup chipGroup, eventTag;
    private Switch switchDay;
    private Button confirm,cancel;
    private String chipContent = "";
    private String updateContent = "";
    private LinearLayout startDateLayout,endDateLayout,tagLayout,participantLayout;
    private ImageView clockIcon,tagIcon,locationIcon,participantIcon,addEventColor,memoIcon;
    private Activity activity = this;
    private List<ActivityInviteBean> inviteList = new ArrayList<>();
    public int timerHour,timerMinute;
    private final Calendar calendar = Calendar.getInstance();
    private TimelineServiceImpl timelineService = new TimelineServiceImpl();
    AsyncTasKHelper.OnResponseListener<TimelineBean,TimelineBean> updateTimelineOnResponseListener = new AsyncTasKHelper.OnResponseListener<TimelineBean, TimelineBean>() {
        @Override
        public Call<ResponseBody<TimelineBean>> request(TimelineBean... timelineBeans) {
            return timelineService.update(timelineBeans[0]);
        }

        @Override
        public void onSuccess(TimelineBean timelineBean) {
            Intent intent = new Intent();
            intent.setClass(EditEventActivity.this,EventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("timelineNo",getIntent().getExtras().getString("timelineNo"));
            String action = getIntent().getExtras().getString("action");
            if (action.equals("meet")){
                bundle.putString("friendId",getIntent().getExtras().getString("friendId"));
            }
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        @Override
        public void onFail(int status, String message) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        editEvent = findViewById(R.id.edit_event);
        eventDate = findViewById(R.id.event_date);
        dateStart = findViewById(R.id.date_start);
        timeStart = findViewById(R.id.time_start);
        dateEnd = findViewById(R.id.date_end);
        timeEnd = findViewById(R.id.time_end);
        eventTag = findViewById(R.id.event_tag);
        moreEventTag = findViewById(R.id.more_event_tag);
        editEventLocation = findViewById(R.id.edit_event_location);
        editColor = findViewById(R.id.edit_color);
        editEventMemo = findViewById(R.id.edit_event_memo);
        editEventParticipant = findViewById(R.id.edit_event_participant);
        clockIcon = findViewById(R.id.clock_icon);
        tagIcon = findViewById(R.id.tag_icon);
        locationIcon = findViewById(R.id.location_icon);
        addEventColor = findViewById(R.id.add_event_color);
        memoIcon = findViewById(R.id.memo_icon);
        startDateLayout = findViewById(R.id.start_date_layout);
        endDateLayout = findViewById(R.id.end_date_layout);
        tagLayout = findViewById(R.id.tag_layout);
        participantLayout = findViewById(R.id.participant_layout);
        switchDay = findViewById(R.id.switch_day);
        switchDay.setOnCheckedChangeListener(switchClickListener);
        participantIcon = findViewById(R.id.participant_icon);
        tagIcon = findViewById(R.id.tag_icon);
        moreEventTag.setOnClickListener(dialogClick);
        Bundle bundle = getIntent().getExtras();
        String timelineNo = bundle.getString("timelineNo");
        String action = bundle.getString("action");
        switch (action) {
            case "meet":
                editEvent.setText(bundle.getString("title"));
                eventDate.setText(bundle.getString("eventDate"));
                editEventLocation.setText(bundle.getString("place"));
                editEventMemo.setText(bundle.getString("addEventMemo"));
//                editColor.setText(); //時間軸顏色

                switchDay.setVisibility(View.GONE);
                startDateLayout.setVisibility(View.GONE);
                endDateLayout.setVisibility(View.GONE);
                tagLayout.setVisibility(View.GONE);
                participantLayout.setVisibility(View.GONE);
                break;
            case "activity" :
                TimelineBean timelineBean = getIntent().getExtras().getParcelable("activityBean");
                editEvent.setText(bundle.getString("title"));
                editEventLocation.setText(bundle.getString("place"));
                editEventMemo.setText(bundle.getString("addEventMemo"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日 E aa h:m", Locale.TAIWAN);
                try {
                    Date start = simpleDateFormat.parse(timelineBean.getStartDate());
                    System.out.println("start = " + start);
                    Date end = simpleDateFormat.parse(timelineBean.getEndDate());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    dateStart.setText(dateFormat.format(start));
                    timeStart.setText(timeFormat.format(start));
                    dateEnd.setText(dateFormat.format(end));
                    timeEnd.setText(timeFormat.format(end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ActivityLabelBean activityLabelBean = timelineBean.getActivityLabelBean();

                if(activityLabelBean != null) {
                    String[] contentArray = activityLabelBean.getContent().split(",");
                    for (String chipString : contentArray) {

                        Chip chip = new Chip(activity);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(activity, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(chipString);
                        if(chipContent.equals("")){
                            chipContent = chipString;
                        }else{
                            chipContent +=","+chipString;
                        }
                        eventTag.addView(chip);
                    }
                }
//                eventTag.setText(timelineBean.getActivityLabelBeanList().get(0).getContent());

                List<ActivityInviteBean> activityInviteBeanList = timelineBean.getActivityInviteBeanList();
//                participantAvatar.setImageBitmap(avatarHelper.getImageResource(timelineBean.getActivityInviteBeanList().get(0).getAvatar()));
                String inviteName = "";
                for (int i = 0; i < activityInviteBeanList.size(); i++) {
                    activityInviteBeanList.get(i).setInvite(true);
                    if(i==0){
                        inviteName = activityInviteBeanList.get(i).getUserName();
                    }else{
                        inviteName +=","+ activityInviteBeanList.get(i).getUserName();
                    }
                }
                ActivityInviteBean.inviteBean = activityInviteBeanList;
                editEventParticipant.setText(inviteName);
//                dateStart.setText();

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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("item.getItemId() = " + item.getItemId());
                switch(item.getItemId()){
                    case R.id.menu_addevent:
                        String action = bundle.getString("action");
                       switch (action){
                           case "meet":
                               String title = editEvent.getText().toString();



                               String place =  editEventLocation.getText().toString();
                               String eventMemo =editEventMemo.getText().toString();
                               TimelineBean meetBean = new TimelineBean();
                               meetBean.setTimelineNo(Integer.parseInt(timelineNo));
                               meetBean.setPlace(place);
                               meetBean.setTitle(title);
                               meetBean.setRemark(eventMemo);
                                AsyncTasKHelper.execute(updateTimelineOnResponseListener,meetBean);
                               break;
                           case "activity":
                               if(dateStart.getText() != null && dateStart.getText().equals("")){
                                   Toast.makeText(activity,"請選擇開始日期",Toast.LENGTH_LONG).show();
                                   break;
                               }
                               if(dateEnd.getText() != null && dateEnd.getText().equals("")){
                                   Toast.makeText(activity,"請選擇結束日期",Toast.LENGTH_LONG).show();
                                   break;
                               }


                               TimelineBean timelineBean = getIntent().getExtras().getParcelable("activityBean");
                               if (switchDay.isChecked()) {
                                   timelineBean.setStartDate(dateStart.getText().toString()+" 00:00");
                                   timelineBean.setEndDate(dateEnd.getText().toString()+" 23:59");
                               }else{
                                   if(timeStart.getText() != null && timeStart.getText().equals("")){
                                       Toast.makeText(activity,"請選擇開始時間",Toast.LENGTH_LONG).show();
                                       break;
                                   }
                                   if(timeEnd.getText() != null && timeEnd.getText().equals("")){
                                       Toast.makeText(activity,"請選擇結束時間",Toast.LENGTH_LONG).show();
                                       break;
                                   }
                                   timelineBean.setStartDate(dateStart.getText().toString()+" "+timeStart.getText().toString());
                                   timelineBean.setEndDate(dateEnd.getText().toString()+" "+timeEnd.getText().toString());
                               }
                               timelineBean.setPlace(editEventLocation.getText().toString());
                               timelineBean.setTitle(editEvent.getText().toString());
                               timelineBean.setRemark(editEventMemo.getText().toString());
                               timelineBean.getActivityLabelBean().setContent(chipContent);
                               timelineBean.setActivityInviteBeanList(inviteList);
                               AsyncTasKHelper.execute(updateTimelineOnResponseListener,timelineBean);

                               break;
                       }
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
                        EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateStart.setText(simpleDateFormat.format(calendar.getTime()));
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
                        EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        year = year;
                        month = month;
                        day = day;
                        calendar.set(year,month,day);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateEnd.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.updateDate(year,month,day);
                datePickerDialog.show();
            }
        });



        //Add Participant
        editEventParticipant = (TextView) findViewById(R.id.edit_event_participant);
        editEventParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventAddParticipantActivity.class);

                startActivityForResult(intent,RequestCode.REQUEST_ADD_PARTICIPANT);
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                timeStart.setText(DateFormat.format("HH:mm",calendar));
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
                        EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0,0,0,timerHour,timerMinute);
                                timeEnd.setText(DateFormat.format("HH:mm",calendar));
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RequestCode.REQUEST_ADD_PARTICIPANT){
            inviteList = ActivityInviteBean.inviteBean;
            String invite = "";
            for (int i = 0; i < inviteList.size(); i++) {
                ActivityInviteBean activityInviteBean = inviteList.get(i);
                if(i==0){
                    invite = activityInviteBean.getUserName();
                }else if(i > 3){
                    invite += "...";
                    break;
                }else{

                    invite += ","+activityInviteBean.getUserName();
                }
            }
            editEventParticipant.setText(invite);

        }
    }
    public CompoundButton.OnCheckedChangeListener switchClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                timeEnd.setVisibility(View.GONE);
                timeStart.setVisibility(View.GONE);
            }else {
                timeEnd.setVisibility(View.VISIBLE);
                timeStart.setVisibility(View.VISIBLE);

            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityInviteBean.inviteBean = null;
    }
    public View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateContent = chipContent;
            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = inflater.inflate(R.layout.event_add_label, null);
            builder.setView(view);
            builder.create();
            AlertDialog alertDialog = builder.show();
            addActivityLabel = (EditText) view.findViewById(R.id.addTag_dialog_Input);
            String[] split = updateContent.split(",");
            chipGroup = (ChipGroup) view.findViewById(R.id.addTag_dialog_selectedBox);

            for (String nowChip : split) {
                Chip chip = new Chip(activity);
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(activity, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                chip.setChipDrawable(chipDrawable);
                chip.setText(nowChip);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(chip);
                        updateContent = updateContent.replaceAll(chip.getText().toString()+",","");
                        updateContent = updateContent.replaceAll(","+chip.getText().toString(),"");
                        updateContent = updateContent.replaceAll(chip.getText().toString(), "");
                    }
                });
                chipGroup.addView(chip);
            }
            addActivityLabel.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN && !addActivityLabel.getText().toString().equals("")) {
                        if(updateContent==null || updateContent.equals("")){
                            updateContent = addActivityLabel.getText().toString();
                        }else{
                            updateContent = updateContent + "," + addActivityLabel.getText().toString();
                        }
                        LayoutInflater chipInflater = LayoutInflater.from(activity);
                        Chip chip = new Chip(activity);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(activity, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(addActivityLabel.getText().toString());
                        chip.setCloseIconVisible(true);

                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroup.removeView(chip);
                                updateContent = updateContent.replaceAll(chip.getText().toString()+",","");
                                updateContent = updateContent.replaceAll(","+chip.getText().toString(),"");
                                updateContent = updateContent.replaceAll(chip.getText().toString(), "");
                            }
                        });
                        chipGroup.addView(chip);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                        addActivityLabel.setText("");
                        return true;
                    }
                    return false;
                }
            });

            confirm = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(updateContent.isEmpty()){
                        Toast.makeText(activity,"請輸入標籤內容，輸入完標籤內容，請先按下鍵盤輸入鍵",Toast.LENGTH_LONG).show();
                        return;
                    }
                    eventTag.removeAllViews();
                    chipContent = updateContent;
                    eventTag.setVisibility(View.VISIBLE);
                    ActivityLabelBean activityLabelBean = new ActivityLabelBean();
                    activityLabelBean.setContent(chipContent);
                    String[] contentArray= chipContent.split(",");

                    for (int i = 0; i < contentArray.length; i++) {
                        Chip chip = new Chip(activity);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(activity, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(contentArray[i]);
                        eventTag.addView(chip);
                    }
                    alertDialog.dismiss();

                }
            });
            cancel = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            });
        }
    };
}



