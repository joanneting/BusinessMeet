package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import tw.com.businessmeet.bean.ActivityDateBean;
import tw.com.businessmeet.helper.DBHelper;

public class ActivityDateDAO {
    private String whereClause = "activity_date_no = ?";
    private String tableName = "activity_date";
    private String[] column = ActivityDateBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ActivityDateDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(ActivityDateBean activityDateBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], activityDateBean.getActivityDateNo());
        values.put(column[1], activityDateBean.getActivityNo());
        values.put(column[2], activityDateBean.getStartDate());
        values.put(column[3], activityDateBean.getEndDate());
        values.put(column[4], activityDateBean.getCreateDate());
        values.put(column[5], activityDateBean.getModifyDate());
        return values;
    }

    public void add(ActivityDateBean activityDateBean) {
        ContentValues values = putValues(activityDateBean);
        db.insert(tableName, null, values);
    }

    public void update(ActivityDateBean activityDateBean) {
        ContentValues values = putValues(activityDateBean);
        db.update(tableName, values, whereClause, new String[]{String.valueOf(activityDateBean.getActivityDateNo())});
    }

    public Cursor search(ActivityDateBean activityDateBean) {
        Integer activityNo = activityDateBean.getActivityNo();

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

    public void delete(Integer activityDateNo) {
        db.delete(tableName, whereClause, new String[]{activityDateNo.toString()});
    }

}
