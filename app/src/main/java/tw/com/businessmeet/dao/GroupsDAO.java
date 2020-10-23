package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupsDAO {
    private String whereClause = "group_no = ?";
    private String tableName = "groups";
    private String[] column = GroupsBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public GroupsDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(GroupsBean groupsBean){
        ContentValues values = new ContentValues();
        values.put(column[0],groupsBean.getGroupNo());
        values.put(column[1],groupsBean.getName());
        values.put(column[2],groupsBean.getUserId());
        values.put(column[3],groupsBean.getCreateDate());
        values.put(column[4],groupsBean.getModifyDate());
        return values;
    }
    public void add(GroupsBean groupsBean){
        ContentValues values = putValues(groupsBean);
        values.put(column[4],dataFormat.format(new Date()));
        db.insert(tableName,null,values);
    }
    public void update(GroupsBean groupsBean){
        ContentValues values = putValues(groupsBean);
        values.put(column[5],dataFormat.format(new Date()));
        db.update(tableName,values,whereClause,new String[]{String.valueOf(groupsBean.getGroupNo())});

    }

    public Cursor search(GroupsBean groupsBean){
        String userId = groupsBean.getUserId();
        String[] searchValue = new String[]{userId};
        String[] searchColumn = new String[]{column[2]};
        String where = "";
        ArrayList<String> args = new ArrayList<>();
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
