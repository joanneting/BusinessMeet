package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.ProblemReportBean;
import tw.com.businessmeet.helper.DBHelper;

public class ProblemReportDAO {
    private String tableName = "problem_report";
    private String[] column = ProblemReportBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProblemReportDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(ProblemReportBean problemReportBean) {
        ContentValues values = new ContentValues();
        values.put(column[0], problemReportBean.getProblemReportNo());
        values.put(column[1], problemReportBean.getContent());
        values.put(column[2], problemReportBean.getUserId());
        values.put(column[3], problemReportBean.getStatus());
        values.put(column[4], problemReportBean.getStartDate());
        values.put(column[5], problemReportBean.getEndDate());
        values.put(column[6], problemReportBean.getCreateDate());
        values.put(column[7], problemReportBean.getModifyDate());
        return values;
    }

    public void add(ProblemReportBean problemReportBean) {
        ContentValues values = putValues(problemReportBean);
        values.put("create_date", dataFormat.format(new Date()));
        db.insert(tableName, null, values);
        db.close();
    }

    public void update(ProblemReportBean problemReportBean) {
        ContentValues values = putValues(problemReportBean);
        values.put("modify_date", dataFormat.format(new Date()));
        db.update(tableName, values, whereClause, new String[]{String.valueOf(problemReportBean.getProblemReportNo())});
        db.close();
    }

    public Cursor search(ProblemReportBean problemReportBean) {
        String userId = problemReportBean.getUserId();
        String[] searchValue = new String[]{userId};
        String[] searchColumn = new String[]{column[2]};
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
