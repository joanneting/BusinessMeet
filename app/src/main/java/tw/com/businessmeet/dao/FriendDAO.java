package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.helper.DBHelper;

public class FriendDAO {
    private String whereClause = "friend_no = ?";
    private String tableName = "friend";
    private String[] column = FriendBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FriendDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(FriendBean friendBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], friendBean.getFriendNo());
        values.put(column[1], friendBean.getMatchmakerId());
        values.put(column[2], friendBean.getFriendId());
        values.put(column[3], friendBean.getRemark());
        values.put(column[4], friendBean.getStatus());
        values.put(column[5], friendBean.getCreateDate());
        values.put(column[6], friendBean.getModifyDate());
        return values;
    }

    public void add(FriendBean friendBean) {
        if (friendBean.getRemark() == null) {
            friendBean.setRemark("");
        }
        ContentValues values = putValues(friendBean);
        values.put(column[5], dataFormat.format(new Date()));
        db.insert(tableName, null, values);
    }

    public void update(FriendBean friendBean) {
        ContentValues values = putValues(friendBean);
        values.put(column[6], dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(friendBean.getFriendNo())});
    }

    public void delete(Integer friendNo) {
        db.delete(tableName, whereClause, new String[]{friendNo.toString()});
    }

    public Cursor search(FriendBean friendBean) {
        String matchmakerId = friendBean.getMatchmakerId();
        String friendId = friendBean.getFriendId();
        String[] searchValue = new String[]{matchmakerId, friendId};
        String[] searchColumn = new String[]{column[1], column[2]};
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
