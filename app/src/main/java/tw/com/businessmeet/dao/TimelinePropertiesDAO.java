package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.TimelinePropertiesBean;
import tw.com.businessmeet.helper.DBHelper;

public class TimelinePropertiesDAO {
    private String tableName = "timeline_properties";
    private String[] column = TimelinePropertiesBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimelinePropertiesDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(TimelinePropertiesBean timelinePropertiesBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], timelinePropertiesBean.getTimelinePropertiesNo());
        values.put(column[1], timelinePropertiesBean.getName());
        values.put(column[2], timelinePropertiesBean.getCreateDate());
        values.put(column[3], timelinePropertiesBean.getModifyDate());
        return values;
    }

    public void add(TimelinePropertiesBean timelinePropertiesBean) {
        ContentValues values = putValues(timelinePropertiesBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(TimelinePropertiesBean timelinePropertiesBean) {
        ContentValues values = putValues(timelinePropertiesBean);
        values.put("modify_date", dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(timelinePropertiesBean.getTimelinePropertiesNo())});
    }

    public Cursor search(TimelinePropertiesBean timelinePropertiesBean) {
        String name = timelinePropertiesBean.getName();
        String[] searchValue = new String[]{name};
        String[] searchColumn = new String[]{column[1]};
        String where = "";
        ArrayList<String> args = new ArrayList<>();
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

    public String getById(Integer timlinePropertiesNo) {
        Cursor cursor = db.query(tableName, null, whereClause, new String[]{timlinePropertiesNo.toString()}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(column[0]);
        try {
            return cursor.getString(cursor.getColumnIndex(column[0]));
        } catch (Exception e) {
            return null;
        }
    }

}
