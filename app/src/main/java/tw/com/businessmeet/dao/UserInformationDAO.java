package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.DBHelper;

public class UserInformationDAO {
    private String whereClause = "user_id = ?";
    private String tableName = "user_information";
    private String[] column = UserInformationBean.getColumn();
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public UserInformationDAO(DBHelper DH) {
        db = DH.getWritableDatabase();
    }

    private ContentValues putValues(UserInformationBean userInformationBean) {
        ContentValues values = new ContentValues();
        String userId = userInformationBean.getUserId();
        String password = userInformationBean.getPassword();
        String name = userInformationBean.getName();
        String gender = userInformationBean.getGender();
        String mail = userInformationBean.getMail();
        String profession = userInformationBean.getProfession();
        String tel = userInformationBean.getTel();
        String avatar = userInformationBean.getAvatar();
        String identifier = userInformationBean.getIdentifier();
        Integer roleNo = userInformationBean.getRoleNo();
        if (userId != null && !userId.equals("")) {
            values.put("user_id", userId);
        }
        if (password != null && !password.equals("")) {
            values.put("password", password);
        }
        if (name != null && !name.equals("")) {
            values.put("name", name);
        }
        if (mail != null && !mail.equals("")) {
            values.put("mail", mail);
        }
        if (gender != null && !gender.equals("")) {
            values.put("gender", gender);
        }
        if (profession != null && !profession.equals("")) {
            values.put("profession", profession);
        }
        if (tel != null && !tel.equals("")) {
            values.put("tel", tel);
        }
        if (avatar != null && !avatar.equals("")) {
            values.put("avatar", avatar);
        }
        if (identifier != null && !identifier.equals("")) {
            values.put("identifier", identifier);
        }
        if (roleNo != null && roleNo != 0) {
            values.put("role_no", roleNo);
        }

        return values;
    }

    public void add(UserInformationBean userInformationBean) {
        ContentValues values = putValues(userInformationBean);

        String createDate = dateFormat.format(new Date());
        values.put("create_date", createDate);
        db.insert(tableName, null, values);

    }

    public void update(UserInformationBean userInformationBean) {

        ContentValues values = putValues(userInformationBean);
        String modifyDate = dateFormat.format(new Date());
        values.put("modify_date", modifyDate);
        //範例
//        String[] whereArgs1 = {"#100", b.getStorage_id()};
//        String whereClause1 = DatabaseSchema.TABLE_TALKS.COLUMN_TID + "=? AND " + DatabaseSchema.TABLE_TALKS.COLUMN_STORAGEID + "=?";
//        db.update(DatabaseSchema.TABLE_TALKS.NAME, values1, whereClause1, whereArgs1);
        db.update(tableName, values, whereClause, new String[]{userInformationBean.getUserId()});
        db.close();
    }

    public void delete(String identifier) {

        db.delete(tableName, whereClause, new String[]{identifier});
        db.close();
    }

    public Cursor getById(String userId) {
        Cursor cursor = db.query(tableName, null, "user_id = ?", new String[]{userId}, null, null, null);
        cursor.moveToFirst();
        try {
            if (cursor.moveToFirst()) {
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId(String identifier) {
        Cursor cursor = db.query(tableName, null, "identifier = ?", new String[]{identifier}, null, null, null);
        cursor.moveToFirst();
        try {
            return cursor.getString(cursor.getColumnIndex("user_id"));
        } catch (Exception e) {
            return null;
        }

    }

    public String getName(String blueTooth) {
        Cursor cursor = db.query(tableName, null, "identifier = ?", new String[]{blueTooth}, null, null, null);
        cursor.moveToFirst();
        try {
            return cursor.getString(cursor.getColumnIndex("name"));
        } catch (Exception e) {
            return null;
        }

    }

    public Cursor searchAll(UserInformationBean userInformationBean) {


        String userId = userInformationBean.getUserId();
        String password = userInformationBean.getPassword();
        String name = userInformationBean.getName();
        String gender = userInformationBean.getGender();
        String mail = userInformationBean.getMail();
        String profession = userInformationBean.getProfession();
        String identifier = userInformationBean.getIdentifier();
        String avatar = userInformationBean.getAvatar();
        String tel = userInformationBean.getTel();
        String[] searchValue = new String[]{userId, password, name, gender, mail, profession, identifier, avatar, tel};
        String[] searchColumn = new String[]{column[0], column[1], column[2], column[3], column[4], column[5], column[6], column[7], column[8]};
        String where = "";
        ArrayList<String> args = new ArrayList<>();

        for (int i = 0; i < searchColumn.length; i++) {
            if (searchValue[i] != null && !searchValue[i].equals("")) {
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


}
