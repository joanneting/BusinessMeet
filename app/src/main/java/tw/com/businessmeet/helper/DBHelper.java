package tw.com.businessmeet.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "BeMet.db";
    private final static String[] _TableName = new String[]{"user_information", "friend", "groups", "friend_group", "friend_customization",
            "timeline", "activity_label", "activity_remind", "activity_invite", "problem_report", "user_role", "activity_date"};
    private Context context;

    public DBHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL = "create table if not exists " + _TableName[0] + "(" +
                "user_id varchar(100) not null  primary key," +
                "password varchar(64) not null," +
                "name nvarchar(100) not null," +
                "gender  char(2) not null," +
                "mail    varchar(100) not null," +
                "profession  nvarchar(100) not null," +
                "avatar varchar(1000) not null," +
                "tel varchar(20), " +
                "role_no int not null," +
                "identifier varchar(36) not null," +
                "firebase_token varchar(255)," +
                "create_date datetime not null," +
                "modify_date datetime" +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[1] + "(" +
                "friend_no   int  primary key," +
                "matchmaker_id  varchar(100) not null references " + _TableName[0] + "(user_id),  " +
                "friend_id  varchar(100) not null references " + _TableName[0] + "(user_id)," +
                "remark nvarchar(2500)," +
                "status int," +
                "create_date datetime not null," +
                "modify_date datetime" +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[2] + "(" +
                "group_no    int    primary key," +
                "name    nvarchar(100) not null,     " +
                "user_id varchar(100) not null references " + _TableName[0] + "(user_id)," +
                "create_date datetime not null," +
                "modify_date datetime    " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[3] + "(" +
                "friend_group_no  int  primary key," +
                "group_no    int not null references " + _TableName[2] + "(group_no),   " +
                "friend_no   int not null references " + _TableName[1] + "(friend_no)," +
                "create_date datetime not null," +
                "modify_date datetime    " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[4] + "(" +
                "friend_customization_no int    primary key," +
                "name    nvarchar(100) not null," +
                "content nvarchar(1000) ," +
                "friend_no   int not null references " + _TableName[1] + "(friend_no)," +
                "create_date datetime not null," +
                "modify_date datetime    " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[5] + "(" +
                "timeline_no int  primary key," +
                "matchmaker_id  varchar(100) not null references " + _TableName[0] + "(user_id),  " +
                "friend_id  varchar(100) not null references " + _TableName[0] + "(user_id)," +
                "place   nvarchar(100) not null," +
                "title   nvarchar(100) ,     " +
                "remark  nvarchar(2500),     " +
                "timeline_properties_no  int not null," +
                "color varchar(7)," +
                "create_date datetime not null," +
                "modify_date datetime" +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[6] + "(" +
                "activity_label_no    int    primary key," +
                "activity_no int not null references " + _TableName[5] + "(timeline_no),  " +
                "content nvarchar(100) not null," +
                "create_date datetime not null," +
                "modify_date datetime        " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[7] + "(" +
                "activity_remind_no  int   primary key," +
                "time datetime not null,     " +
                "activity_no int not null references " + _TableName[5] + "(timeline_no)," +
                "create_date datetime not null ," +
                "modify_date datetime    " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[8] + "(" +
                "activity_invite_no   int  primary key," +
                "user_id varchar(100) not null references " + _TableName[0] + "(user_id),  " +
                "activity_no int not null references " + _TableName[5] + "(timeline_no)," +
                "create_date datetime not null ," +
                "modify_date datetime    " +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[9] + "(" +
                "problem_report_no   int  primary key," +
                "content nvarchar(3000) not null ," +
                "user_id varchar(100) not null references " + _TableName[0] + "(user_id)," +
                "status varchar(3)," +
                "start_date datetime," +
                "end_date datetime," +
                "create_date datetime not null," +
                "modify_date datetime" +
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[10] + "(" +
                "role_no int primary key," +
                "role_name varchar(10)" +
                ");";
        db.execSQL(SQL);
        SQL = "insert into " + _TableName[10] + "(role_no,role_name)" +
                "values(1,'admin'),(2,'manage'),(3,'user');";
        db.execSQL(SQL);
        SQL = "create table if not exists " + _TableName[11] + "(" +
                "activity_date_no int primary key," +
                "activity_no int  not null references " + _TableName[5] + "(timeline_no)," +
                "start_date datetime," +
                "end_date datetime," +
                "create_date datetime not null," +
                "modify_date datetime" +
                ");";
        db.execSQL(SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String s : _TableName) {
            final String SQL = "DROP TABLE " + s;
            db.execSQL(SQL);
        }

    }

    public Context getContext() {
        return context;
    }
}
