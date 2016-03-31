package texium.mx.drones.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by texiumuser on 16/03/2016.
 */
public class BDTasksManager extends SQLiteOpenHelper {


    public static final String TASK_DETAILS_TABLE_NAME = "Task_Details";
    public static final String TASKS_TABLE_NAME = "Tasks";
    public static final String USERS_TABLE_NAME = "Users";

    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String BLOB_TYPE = "blob";
    public static final String REAL_TYPE = "real";
    public static final String NUMERIC_TYPE = "numeric";

    public static class ColumnTaskDetails {
        public static final String TASK_DETAIL_CVE = "cve_task_detail";
        public static final String TASK_ID = "task_id";
        public static final String TASK_STATUS = "task_status";
    }

    public static class ColumnTasks {
        public static final String TASK_CVE = "task_cve";
        public static final String TASK_TITLE = "task_title";
        public static final String TASK_CONTENT = "task_content";
        public static final String TASK_PRIORITY = "task_priority";
        public static final String TASK_BEGIN_DATE = "task_begin_date";
        public static final String TASK_END_DATE = "task_end_date";
        public static final String TASK_ID = "task_id";
        public static final String TASK_LATITUDE = "task_latitude";
        public static final String TASK_LONGITUDE = "task_longitude";
        public static final String TASK_STATUS = "task_status";
        public static final String TASK_USER_ID = "task_user_id";
    }

    public static class ColumnUsers {
        public static final String USER_CVE = "cve_user";
        public static final String USER_ID = "idUser";
        public static final String USERNAME = "userName";
        public static final String ACTOR_ID = "idActor";
        public static final String ACTOR_NAME = "actorName";
        public static final String ACTOR_TYPE = "actorType";
        public static final String ACTOR_TYPE_NAME = "actorTypeName";
        public static final String TEAM_ID = "idTeam";
        public static final String TEAM_NAME = "teamName";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LAST_TEAM_CONNECTION = "lastTeamConnection";
        public static final String PASSWORD = "password";

    }

    public static final String CREATE_TASKS_TABLE_SCRIPT =
            "create table " + TASKS_TABLE_NAME + "(" +
                    ColumnTasks.TASK_CVE + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnTasks.TASK_TITLE + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_CONTENT + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_PRIORITY + " " + INT_TYPE + "," +
                    ColumnTasks.TASK_BEGIN_DATE + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_END_DATE + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_ID + " " + INT_TYPE + "," +
                    ColumnTasks.TASK_LATITUDE + " " + REAL_TYPE + "," +
                    ColumnTasks.TASK_LONGITUDE  + " " + REAL_TYPE + "," +
                    ColumnTasks.TASK_STATUS + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_USER_ID + " " + INT_TYPE +
            ")";

    public static final String CREATE_USERS_TABLE_SCRIPT =
            "create table " + USERS_TABLE_NAME + "(" +
                    ColumnUsers.USER_CVE + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnUsers.USER_ID + " " + INT_TYPE + "," +
                    ColumnUsers.USERNAME + " " + STRING_TYPE + "," +
                    ColumnUsers.ACTOR_ID + " " + INT_TYPE + "," +
                    ColumnUsers.ACTOR_NAME + " " + STRING_TYPE + "," +
                    ColumnUsers.ACTOR_TYPE + " " + STRING_TYPE + "," +
                    ColumnUsers.ACTOR_TYPE_NAME + " " + INT_TYPE + "," +
                    ColumnUsers.TEAM_ID + " " + INT_TYPE + "," +
                    ColumnUsers.TEAM_NAME  + " " + STRING_TYPE + "," +
                    ColumnUsers.LATITUDE + " " + REAL_TYPE + "," +
                    ColumnUsers.LONGITUDE + " " + REAL_TYPE + "," +
                    ColumnUsers.LAST_TEAM_CONNECTION + " " + STRING_TYPE + "," +
                    ColumnUsers.PASSWORD + " " + STRING_TYPE +
                    ")";

    //TODO insert user script

    public BDTasksManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE_SCRIPT);
        db.execSQL(CREATE_USERS_TABLE_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Users");

        db.execSQL(CREATE_TASKS_TABLE_SCRIPT);
        db.execSQL(CREATE_USERS_TABLE_SCRIPT);
    }
}
