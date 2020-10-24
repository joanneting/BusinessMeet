package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.UserCustomizationBean;
import tw.com.businessmeet.helper.DBHelper;

public class UserCustomizationDAO {
    private String tableName = "user_customization";
    private String[] column = UserCustomizationBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public UserCustomizationDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(UserCustomizationBean userCustomizationBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], userCustomizationBean.getUserCustomizationNo());
        values.put(column[1], userCustomizationBean.getUserId());
        values.put(column[2], userCustomizationBean.getColumnName());
        values.put(column[3], userCustomizationBean.getContent());
        values.put(column[4], userCustomizationBean.getCreateDate());
        values.put(column[5], userCustomizationBean.getModifyDate());
        return values;
    }

    public void add(UserCustomizationBean userCustomizationBean) {
        ContentValues values = putValues(userCustomizationBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(UserCustomizationBean userCustomizationBean) {
        ContentValues values = putValues(userCustomizationBean);
        values.put("modify_date", dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(userCustomizationBean.getUserCustomizationNo())});
    }

    public Cursor search(UserCustomizationBean userCustomizationBean) {
        String userId = userCustomizationBean.getUserId();
        String[] searchValue = new String[]{userId};
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

    public String getById(Integer userCustomizationNo) {
        Cursor cursor = db.query(tableName, null, whereClause, new String[]{userCustomizationNo.toString()}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(column[0]);
        try {
            return cursor.getString(cursor.getColumnIndex(column[0]));
        } catch (Exception e) {
            return null;
        }
    }

}
