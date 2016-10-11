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
    public static final String TASKS_FILES_TABLE_NAME = "Tasks_Files";
    public static final String FILES_PACKAGES_TABLE_NAME = "Files_Packages";
    public static final String LINKS_TABLE_NAME = "Links";
    public static final String APP_VERSION_TABLE_NAME = "App_Version";

    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String BLOB_TYPE = "blob";
    public static final String REAL_TYPE = "real";
    public static final String NUMERIC_TYPE = "numeric";

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
        //public static final String TEAM_ID = "idTeam";
    }

    public static class ColumnUsers {
        public static final String USER_CVE = "cve_user";
        public static final String USER_ID = "idUser";
        public static final String USERNAME = "userName";
        public static final String ACTOR_ID = "idActor";
        public static final String ACTOR_NAME = "actorName";
        public static final String ACTOR_TYPE = "actorType";
        public static final String ACTOR_TYPE_NAME = "actorTypeName";
        //public static final String TEAM_ID = "idTeam";
        //public static final String TEAM_NAME = "teamName";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LAST_TEAM_CONNECTION = "lastTeamConnection";
        public static final String PASSWORD = "password";

    }

    public static class ColumnTaskDetails {
        public static final String TASK_DETAIL_CVE = "task_detail_cve";
        public static final String TASK_ID = "task_id";
        public static final String TASK_STATUS = "task_status";
        public static final String TASK_USER_ID = "task_user_id";
        public static final String SERVER_SYNC = "server_sync";
        public static final String TASK_COMMENT = "task_comment";
    }

    public static class ColumnTasksFiles {
        public static final String TASK_FILE_CVE = "task_file_cve";
        public static final String TASK_DETAIL_CVE = "task_detail_cve";
        public static final String TASK_FILE_ID = "task_file_id";
        public static final String BASE_FILE = "base_file";
        public static final String FILE_TYPE = "file_type";
        public static final String DESCRIPTION_FILE = "description_file";
        public static final String SERVER_SYNC = "server_sync";
        public static final String FILE_STATUS = "file_status";
        public static final String LOCAL_URI = "local_uri";
    }

    public static class ColumnFilePackages {
        public static final String FILE_PACKAGE_CVE = "file_package_cve";
        public static final String BASE_PACKAGE = "base_package";
        public static final String FILE_TYPE = "file_type";
        public static final String TEMP_KEY_ID = "temp_key_id";
    }

    public static  class ColumnLinks {
        public static final String LINKS_CVE = "link_cve";
        public static final String LINK = "link";
        public static final String USER_ID = "user_id";
        public static final String CREATION_DATE = "creation_date";
        public static final String UPDATE_DATE = "update_date";
        public static final String LINK_STATUS = "link_status";
    }

    public static class ColumnAppVersion {
        public static final String APP_VERSION_CVE = "app_version_cve";
        public static final String APP_VERSION = "app_version";
        public static final String VERSION_MSG = "version_msg";
    }

    public static final String CREATE_TASKS_TABLE_SCRIPT =
            "create table " + TASKS_TABLE_NAME + "(" +
                    ColumnTasks.TASK_CVE + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnTasks.TASK_TITLE + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_CONTENT + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_PRIORITY + " " + INT_TYPE + "," +
                    ColumnTasks.TASK_BEGIN_DATE + " " + STRING_TYPE     + "," +
                    ColumnTasks.TASK_END_DATE + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_ID + " " + INT_TYPE + "," +
                    ColumnTasks.TASK_LATITUDE + " " + REAL_TYPE + "," +
                    ColumnTasks.TASK_LONGITUDE  + " " + REAL_TYPE + "," +
                    ColumnTasks.TASK_STATUS + " " + STRING_TYPE + "," +
                    ColumnTasks.TASK_USER_ID + " " + INT_TYPE +
                    //ColumnTasks.TEAM_ID + " " + INT_TYPE +
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
                    //ColumnUsers.TEAM_ID + " " + INT_TYPE + "," +
                    //ColumnUsers.TEAM_NAME  + " " + STRING_TYPE + "," +
                    ColumnUsers.LATITUDE + " " + REAL_TYPE + "," +
                    ColumnUsers.LONGITUDE + " " + REAL_TYPE + "," +
                    ColumnUsers.LAST_TEAM_CONNECTION + " " + STRING_TYPE + "," +
                    ColumnUsers.PASSWORD + " " + STRING_TYPE +
                    ")";

    public static final String CREATE_TASKS_FILES_TABLE_SCRIPT =
            "create table " + TASKS_FILES_TABLE_NAME + "(" +
                    ColumnTasksFiles.TASK_FILE_CVE + " " + INT_TYPE + " primary key autoincrement, " +
                    ColumnTasksFiles.TASK_FILE_ID + " " + INT_TYPE + "," +
                    ColumnTasksFiles.TASK_DETAIL_CVE + " " + INT_TYPE + "," +
                    ColumnTasksFiles.BASE_FILE + " " + STRING_TYPE + "," +
                    ColumnTasksFiles.FILE_TYPE + " " + INT_TYPE + "," +
                    ColumnTasksFiles.DESCRIPTION_FILE + " " + STRING_TYPE + "," +
                    ColumnTasksFiles.SERVER_SYNC + " " + INT_TYPE + "," +
                    ColumnTasksFiles.FILE_STATUS + " " + INT_TYPE + "," +
                    ColumnTasksFiles.LOCAL_URI + " " + INT_TYPE +
            ")";

    public static final  String CREATE_TASK_DETAILS_TABLE_SCRIPT =
            "create table " + TASK_DETAILS_TABLE_NAME + "(" +
                    ColumnTaskDetails.TASK_DETAIL_CVE + " " + INT_TYPE + " primary key autoincrement, " +
                    ColumnTaskDetails.TASK_ID + " " + INT_TYPE  + "," +
                    ColumnTaskDetails.TASK_STATUS + " " + INT_TYPE + "," +
                    ColumnTaskDetails.TASK_USER_ID + " " + INT_TYPE + "," +
                    ColumnTaskDetails.SERVER_SYNC + " " + INT_TYPE + "," +
                    ColumnTaskDetails.TASK_COMMENT + " " + STRING_TYPE + " " +
            ")";

    public static final String CREATE_FILES_PACKAGE_TABLE_SCRIPT =
            "create table " + FILES_PACKAGES_TABLE_NAME + "(" +
                    ColumnFilePackages.FILE_PACKAGE_CVE + " " + INT_TYPE + " primary key autoincrement, " +
                    ColumnFilePackages.BASE_PACKAGE + " " + STRING_TYPE + "," +
                    ColumnFilePackages.FILE_TYPE + " " + INT_TYPE + "," +
                    ColumnFilePackages.TEMP_KEY_ID + " " + INT_TYPE +
                    ")";

    public static final String CREATE_LINKS_TABLE_SCRIPT =
            "create table " + LINKS_TABLE_NAME + "(" +
                    ColumnLinks.LINKS_CVE + " " + INT_TYPE + " primary key autoincrement, " +
                    ColumnLinks.LINK + " " + STRING_TYPE + "," +
                    ColumnLinks.CREATION_DATE + " " + STRING_TYPE + "," +
                    ColumnLinks.UPDATE_DATE + " " + STRING_TYPE + "," +
                    ColumnLinks.USER_ID + " " + INT_TYPE + "," +
                    ColumnLinks.LINK_STATUS + " " + INT_TYPE +
                    ")";

    public static final String CREATE_APP_VERSION_TABLE_SCRIPT =
            "create table " + APP_VERSION_TABLE_NAME + "(" +
                    ColumnAppVersion.APP_VERSION_CVE + " " + INT_TYPE + " primary key autoincrement, " +
                    ColumnAppVersion.APP_VERSION + " " + STRING_TYPE + "," +
                    ColumnAppVersion.VERSION_MSG + " " + STRING_TYPE +
                            ")";

    public static  final String DROP_TABLE_IF_EXISTS = "drop table if exists ";

    public BDTasksManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE_SCRIPT);
        db.execSQL(CREATE_USERS_TABLE_SCRIPT);
        db.execSQL(CREATE_TASK_DETAILS_TABLE_SCRIPT);
        db.execSQL(CREATE_TASKS_FILES_TABLE_SCRIPT);
        db.execSQL(CREATE_FILES_PACKAGE_TABLE_SCRIPT);
        db.execSQL(CREATE_LINKS_TABLE_SCRIPT);
        db.execSQL(CREATE_APP_VERSION_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASKS_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.USERS_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASK_DETAILS_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASKS_FILES_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.FILES_PACKAGES_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.LINKS_TABLE_NAME);
        db.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.APP_VERSION_TABLE_NAME);

        db.execSQL(CREATE_TASKS_TABLE_SCRIPT);
        db.execSQL(CREATE_USERS_TABLE_SCRIPT);
        db.execSQL(CREATE_TASK_DETAILS_TABLE_SCRIPT);
        db.execSQL(CREATE_TASKS_FILES_TABLE_SCRIPT);
        db.execSQL(CREATE_FILES_PACKAGE_TABLE_SCRIPT);
        db.execSQL(CREATE_LINKS_TABLE_SCRIPT);
        db.execSQL(CREATE_APP_VERSION_TABLE_SCRIPT);
    }
}
