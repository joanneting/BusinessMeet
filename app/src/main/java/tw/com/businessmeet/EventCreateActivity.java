package tw.com.businessmeet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.ActivityLabelBean;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.dao.ActivityLabelDAO;
import tw.com.businessmeet.dao.TimelineDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class EventCreateActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView dateStart, dateEnd, timeStart, timeEnd, addColor, addParticipant;
    private TextView event, date, moreEventTag, title, addEventMemo, addLocation;
    private EditText addActivityLabel;
    private Switch switchDay;
    public int timerHour, timerMinute;
    private final Calendar calendar = Calendar.getInstance();
    private Context context;
    private TimelineDAO timelineDAO;
    private List<ActivityInviteBean> activityInviteBeanList = new ArrayList<>();
    //chip
    private ChipGroup chipGroup, eventLabel;
    private String chipContent = "";
    private String updateContent = "";
    private Button confirm, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        context = this;
        DBHelper dbHelper = new DBHelper(this);
        timelineDAO = new TimelineDAO(dbHelper);
        ActivityLabelDAO activityLabelDAO = new ActivityLabelDAO(dbHelper);
        //Event = (TextView) findViewById(R.);
//        addEventLocation = findViewById(R.id.add_event_location);
        switchDay = findViewById(R.id.switch_day);
        dateStart = findViewById(R.id.date_start);
        dateEnd = findViewById(R.id.date_end);
        timeStart = findViewById(R.id.time_start);
        timeEnd = findViewById(R.id.time_end);
        addEventMemo = findViewById(R.id.add_event_memo);
        eventLabel = findViewById(R.id.event_label);
        eventLabel.setVisibility(View.GONE);
        moreEventTag = findViewById(R.id.more_event_tag);
        moreEventTag.setOnClickListener(dialogClick);
        title = findViewById(R.id.add_event);
        switchDay = findViewById(R.id.switch_day);
        switchDay.setOnCheckedChangeListener(switchClickListener);
        //toolbar
        toolbar = findViewById(R.id.event_create_toolbar);
        toolbar.inflateMenu(R.menu.event_create_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_addevent) {
                if (dateStart.getText() != null && dateStart.getText().equals("")) {
                    Toast.makeText(context, "請選擇開始日期", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (dateEnd.getText() != null && dateEnd.getText().equals("")) {
                    Toast.makeText(context, "請選擇結束日期", Toast.LENGTH_LONG).show();
                    return false;
                }


                TimelineBean timelineBean = new TimelineBean();
                timelineBean.setMatchmakerId(DeviceHelper.getUserId(this));
                if (switchDay.isChecked()) {
                    timelineBean.setStartDate(dateStart.getText().toString() + " 00:00");
                    timelineBean.setEndDate(dateEnd.getText().toString() + " 23:59");
                } else {
                    if (timeStart.getText() != null && timeStart.getText().equals("")) {
                        Toast.makeText(context, "請選擇開始時間", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (timeEnd.getText() != null && timeEnd.getText().equals("")) {
                        Toast.makeText(context, "請選擇結束時間", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    timelineBean.setStartDate(dateStart.getText().toString() + " " + timeStart.getText().toString());
                    timelineBean.setEndDate(dateEnd.getText().toString() + " " + timeEnd.getText().toString());
                }
                timelineBean.setPlace(addLocation.getText().toString());
//                        timelineBean.setPlace("place");
                timelineBean.setTitle(title.getText().toString());
                timelineBean.setTimelinePropertiesNo(1);
                timelineBean.setRemark(addEventMemo.getText().toString());
                ActivityLabelBean activityLabelBean = new ActivityLabelBean();
                activityLabelBean.setContent(chipContent);
                timelineBean.setActivityLabelBean(activityLabelBean);
                timelineBean.setActivityInviteBeanList(activityInviteBeanList);
                AsyncTaskHelper.execute(() -> TimelineServiceImpl.add(timelineBean), timelineDAO::add);
                Intent intent = new Intent();
                intent.setClass(this, SelfIntroductionActivity.class);
                startActivity(intent);
                finish();
            }
            return false;
        });


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateStart.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.updateDate(year, month, day);
                datePickerDialog.show();
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateEnd.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.updateDate(year, month, day);
                datePickerDialog.show();
            }
        });

        //Add Color
        addColor = findViewById(R.id.add_color);
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
        addLocation = findViewById(R.id.add_event_location);
//        addLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), EventAddLocationActivity.class);
//                startActivity(i);
//            }
//        });

        //Add Participant
        addParticipant = findViewById(R.id.add_event_participant);
        addParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventAddParticipantActivity.class);

                startActivityForResult(intent, RequestCode.REQUEST_ADD_PARTICIPANT);
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0, 0, 0, timerHour, timerMinute);
                                timeStart.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, false

                );
                //Displayed previous selected time
                timePickerDialog.updateTime(timerHour, timerMinute);
                //Show dialog
                timePickerDialog.show();

            }
        });


        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timerHour = hourOfDay;
                                timerMinute = minute;
                                //Set hour and minute
                                calendar.set(0, 0, 0, timerHour, timerMinute);
                                timeEnd.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, false

                );
                //Displayed previous selected time
                timePickerDialog.updateTime(timerHour, timerMinute);
                //Show dialog
                timePickerDialog.show();

            }
        });

    }

    public View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateContent = chipContent;
            LayoutInflater inflater = getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(EventCreateActivity.this);
            View view = inflater.inflate(R.layout.event_add_label, null);
            builder.setView(view);
            builder.create();
            AlertDialog alertDialog = builder.show();
            addActivityLabel = view.findViewById(R.id.addTag_dialog_Input);

            chipGroup = view.findViewById(R.id.addTag_dialog_selectedBox);
            if (!updateContent.equals("")) {
                String[] split = updateContent.split(",");
                for (String nowChip : split) {
                    Chip chip = new Chip(EventCreateActivity.this);
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(EventCreateActivity.this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                    chip.setChipDrawable(chipDrawable);
                    chip.setText(nowChip);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                            updateContent = updateContent.replaceAll(chip.getText().toString() + ",", "");
                            updateContent = updateContent.replaceAll("," + chip.getText().toString(), "");
                            updateContent = updateContent.replaceAll(chip.getText().toString(), "");

                        }
                    });
                    chipGroup.addView(chip);
                }
            }
            addActivityLabel.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (updateContent == null || updateContent.equals("")) {
                            updateContent = addActivityLabel.getText().toString();
                        } else {
                            updateContent = updateContent + "," + addActivityLabel.getText().toString();
                        }
                        LayoutInflater chipInflater = LayoutInflater.from(EventCreateActivity.this);
                        Chip chip = new Chip(EventCreateActivity.this);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(EventCreateActivity.this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(addActivityLabel.getText().toString());
                        chip.setCloseIconVisible(true);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroup.removeView(chip);
                                updateContent = updateContent.replaceAll(chip.getText().toString() + ",", "");
                                updateContent = updateContent.replaceAll("," + chip.getText().toString(), "");
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

            confirm = view.findViewById(R.id.addColumn_dialog_confirmButton);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (updateContent.isEmpty()) {
                        Toast.makeText(EventCreateActivity.this, "請輸入標籤內容，輸入完標籤內容，請先按下鍵盤輸入鍵", Toast.LENGTH_LONG).show();
                        return;
                    }
                    chipContent = updateContent;
                    ActivityLabelBean activityLabelBean = new ActivityLabelBean();
                    activityLabelBean.setContent(chipContent);
                    eventLabel.removeAllViews();
                    eventLabel.setVisibility(View.VISIBLE);
                    String[] contentArray = chipContent.split(",");
                    for (int i = 0; i < contentArray.length; i++) {
                        Chip chip = new Chip(EventCreateActivity.this);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(EventCreateActivity.this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(contentArray[i]);
                        eventLabel.addView(chip);
                    }
                    alertDialog.dismiss();

                }
            });
            cancel = view.findViewById(R.id.addColumn_dialog_cancelButton);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequestCode.REQUEST_ADD_PARTICIPANT) {
            activityInviteBeanList = ActivityInviteBean.inviteBean;
            String invite = "";
            for (int i = 0; i < activityInviteBeanList.size(); i++) {
                ActivityInviteBean activityInviteBean = activityInviteBeanList.get(i);
                if (i == 0) {
                    invite = activityInviteBean.getUserName();
                } else if (i > 3) {
                    invite += "...";
                    break;
                } else {

                    invite += "," + activityInviteBean.getUserName();
                }
            }
            addParticipant.setText(invite);

        }
    }

    public CompoundButton.OnCheckedChangeListener switchClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                timeEnd.setVisibility(View.GONE);
                timeStart.setVisibility(View.GONE);
            } else {
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
}
