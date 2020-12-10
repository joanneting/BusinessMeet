package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.helper.DBHelper;

public class ActivityInviteDAO {
    private String whereClause = "activity_invite_no = ?";
    private String tableName = "activity_invite";
    private String[] column = ActivityInviteBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ActivityInviteDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(ActivityInviteBean activityInviteBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], activityInviteBean.getActivityInviteNo());
        values.put(column[1], activityInviteBean.getUserId());
        values.put(column[2], activityInviteBean.getActivityNo());
        values.put(column[3], activityInviteBean.getCreateDate());
        values.put(column[4], activityInviteBean.getModifyDate());
        return values;
    }

    public void add(ActivityInviteBean activityInviteBean) {
        ContentValues values = putValues(activityInviteBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(ActivityInviteBean activityInviteBean) {
        ContentValues values = putValues(activityInviteBean);
        db.update(tableName, values, whereClause, new String[]{String.valueOf(activityInviteBean.getActivityInviteNo())});
    }

    public Cursor search(ActivityInviteBean activityInviteBean) {
        Integer activityNo = activityInviteBean.getActivityNo();

        Integer[] searchValue = new Integer[]{activityNo};
        String[] searchColumn = new String[]{column[2]};
        String where = "";
        ArrayList<Integer> args = new ArrayList<>();
        for (int i = 0; i < searchColumn.length; i++) {
            if (!searchValue[i].equals("") && searchValue[i] != null) {
                if (!where.equals("")) {
                    where += " and ";
                }
                where += searchColumn[i] + " = ?";
                args.add(searchValue[i]);
            }
        }
        Cursor cursor = db.query(tableName, column, where, args.toArray(new String[0]), null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
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
