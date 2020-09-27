package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimelineDAO {
    private String tableName = "timeline";
    private String[] column = TimelineBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public TimelineDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(TimelineBean timelineBean){
        ContentValues values = new ContentValues();
        values.put(column[0],timelineBean.getTimelineNo());
        values.put(column[1],timelineBean.getMatchmakerId());
        values.put(column[2],timelineBean.getFriendId());
        values.put(column[3],timelineBean.getPlace());
        values.put(column[4],timelineBean.getTitle());
        values.put(column[5],timelineBean.getRemark());
        values.put(column[6],timelineBean.getTimelinePropertiesNo());
        values.put(column[7],timelineBean.getColor());
        values.put(column[8],timelineBean.getCreateDateStr());
        values.put(column[9],timelineBean.getModifyDateStr());
        return values;
    }
    public void add(TimelineBean timelineBean){
        String createDate = dateFormat.format(new Date());
        timelineBean.setCreateDateStr(createDate);
        ContentValues values = putValues(timelineBean);

        db.insert(tableName,null,values);
    }
    public void update(TimelineBean timelineBean){
        timelineBean.setModifyDateStr(dateFormat.format(new Date()));
        ContentValues values = putValues(timelineBean);
        db.update(tableName,values,whereClause,new String[]{String.valueOf(timelineBean.getTimelineNo())});

    }

    public Cursor search(TimelineBean timelineBean){
        String matchmakerId = timelineBean.getMatchmakerId();
        String friendId = timelineBean.getFriendId();
        String createDate = timelineBean.getCreateDateStr();
        String[] searchValue = new String[]{matchmakerId,friendId,createDate};
        String[] searchColumn = new String[]{column[1],column[2],column[8]};
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
