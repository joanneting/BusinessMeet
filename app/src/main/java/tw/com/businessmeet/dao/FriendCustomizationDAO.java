package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.helper.DBHelper;

public class FriendCustomizationDAO {
    private String whereClause = "friend_customization_no = ?";
    private String tableName = "friend_customization";
    private String[] column = FriendCustomizationBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FriendCustomizationDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(FriendCustomizationBean friendCustomizationBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], friendCustomizationBean.getFriendCustomizationNo());
        values.put(column[1], friendCustomizationBean.getName());
        values.put(column[2], friendCustomizationBean.getFriendNo());
        values.put(column[3], friendCustomizationBean.getCreateDate());
        values.put(column[4], friendCustomizationBean.getModifyDate());
        return values;
    }

    public void add(FriendCustomizationBean friendCustomizationBean) {
        ContentValues values = putValues(friendCustomizationBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(FriendCustomizationBean friendCustomizationBean) {
        ContentValues values = putValues(friendCustomizationBean);
        values.put("modify_date", dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(friendCustomizationBean.getFriendCustomizationNo())});
    }

    public Cursor search(FriendCustomizationBean friendCustomizationBean) {
        Integer friendNo = friendCustomizationBean.getFriendNo();
        Integer[] searchValue = new Integer[]{friendNo};
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
