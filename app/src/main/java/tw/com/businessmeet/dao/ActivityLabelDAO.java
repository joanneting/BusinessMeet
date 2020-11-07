package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import tw.com.businessmeet.bean.ActivityLabelBean;
import tw.com.businessmeet.helper.DBHelper;

public class ActivityLabelDAO {
    private String whereClause = "activity_label_no = ?";
    private String tableName = "activity_label";
    private String[] column = ActivityLabelBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ActivityLabelDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(ActivityLabelBean activityLabelBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], activityLabelBean.getActivityLabelNo());
        values.put(column[1], activityLabelBean.getActivityNo());
        values.put(column[2], activityLabelBean.getContent());
        values.put(column[3], activityLabelBean.getCreateDate());
        values.put(column[4], activityLabelBean.getModifyDate());
        return values;
    }

    public void add(ActivityLabelBean activityLabelBean) {
        ContentValues values = putValues(activityLabelBean);
        db.insert(tableName, null, values);
    }

    public void update(ActivityLabelBean activityLabelBean) {
        ContentValues values = putValues(activityLabelBean);
        db.update(tableName, values, whereClause, new String[]{String.valueOf(activityLabelBean.getActivityLabelNo())});
    }

    public Cursor search(ActivityLabelBean activityLabelBean) {
        Integer activityNo = activityLabelBean.getActivityNo();

        Integer[] searchValue = new Integer[]{activityNo};
        String[] searchColumn = new String[]{column[1]};
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
