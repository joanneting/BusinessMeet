package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import tw.com.businessmeet.bean.ActivityRemindBean;
import tw.com.businessmeet.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityRemindDAO {
    private String whereClause = "activity_remind_no = ?";
    private String tableName = "activity_remind";
    private String[] column = ActivityRemindBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ActivityRemindDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(ActivityRemindBean activityRemindBean){
        ContentValues values = new ContentValues();
        values.put(column[0],activityRemindBean.getActivityRemindNo());
        values.put(column[1],activityRemindBean.getTime());
        values.put(column[2],activityRemindBean.getActivityNo());
        values.put(column[3],activityRemindBean.getCreateDate());
        values.put(column[4],activityRemindBean.getModifyDate());
        return values;
    }
    public void add(ActivityRemindBean activityRemindBean){
        ContentValues values = putValues(activityRemindBean);
        values.put("create_date",dataFormat.format(new Date()));
        db.insert(tableName,null,values);
    }
    public void update(ActivityRemindBean activityRemindBean){
        ContentValues values = putValues(activityRemindBean);
        values.put("modify_date",dataFormat.format(new Date()));
        db.update(tableName,values,whereClause,new String[]{String.valueOf(activityRemindBean.getActivityRemindNo())});

    }

    public Cursor search(ActivityRemindBean activityRemindBean){
        Integer activityNo = activityRemindBean.getActivityNo();

        Integer[] searchValue = new Integer[]{activityNo};
        String[] searchColumn = new String[]{column[2]};
        String where = "";
        ArrayList<Integer> args = new ArrayList<>();
        for(int i = 0; i < searchColumn.length; i ++){
            if(!searchValue[i].equals("") && searchValue[i] != null){
                if(!where.equals("")) where += " and ";
                where += searchColumn[i] + " = ?";
                args.add(searchValue[i]);
            }
        }
        Cursor cursor = db.query(tableName, column, where, args.toArray(new String[0]),null,null,null);
        if(cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }

    // getById

//    public String getById(String blueTooth) {
//        Cursor cursor = db.query(tableName, null, "blue_tooth = ?", new String[]{blueTooth}, null, null, null);
//        cursor.moveToFirst();
//        int index = cursor.getColumnIndex("blue_tooth");
//        try {
//            return cursor.getString(cursor.getColumnIndex("blue_tooth"));
//        } catch (Exception e) {
//            return null;
//        }
//    }

}
