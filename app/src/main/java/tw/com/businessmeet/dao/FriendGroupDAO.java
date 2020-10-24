package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.helper.DBHelper;

public class FriendGroupDAO {
    private String tableName = "friend_group";
    private String[] column = FriendGroupBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FriendGroupDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(FriendGroupBean friendGroupBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], friendGroupBean.getFriendGroupNo());
        values.put(column[1], friendGroupBean.getGroupNo());
        values.put(column[2], friendGroupBean.getFriendNo());
        values.put(column[3], friendGroupBean.getCreateDate());
        values.put(column[4], friendGroupBean.getModifyDate());
        return values;
    }

    public void add(FriendGroupBean friendGroupBean) {
        ContentValues values = putValues(friendGroupBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(FriendGroupBean friendGroupBean) {
        ContentValues values = putValues(friendGroupBean);
        values.put("modify_date", dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(friendGroupBean.getFriendGroupNo())});
    }

    public Cursor search(FriendGroupBean friendGroupBean) {
        Integer groupNo = friendGroupBean.getGroupNo();
        Integer friendNo = friendGroupBean.getFriendNo();
        Integer[] searchValue = new Integer[]{groupNo, friendNo};
        String[] searchColumn = new String[]{column[1], column[2]};
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
