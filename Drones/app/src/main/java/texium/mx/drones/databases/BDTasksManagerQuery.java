package texium.mx.drones.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.SyncTaskServer;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 17/03/2016.
 */
public class BDTasksManagerQuery {

    static String BDName = "BDTasksManager";
    static Integer BDVersion = 19;

    public static void addTask(Context context, Tasks t) throws Exception {
        try{
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasks.TASK_TITLE, t.getTask_tittle());
            cv.put(BDTasksManager.ColumnTasks.TASK_CONTENT, t.getTask_content());
            cv.put(BDTasksManager.ColumnTasks.TASK_PRIORITY, t.getTask_priority());
            cv.put(BDTasksManager.ColumnTasks.TASK_BEGIN_DATE, t.getTask_begin_date());
            cv.put(BDTasksManager.ColumnTasks.TASK_END_DATE, t.getTask_end_date());
            cv.put(BDTasksManager.ColumnTasks.TASK_ID, t.getTask_id());
            cv.put(BDTasksManager.ColumnTasks.TASK_LATITUDE, t.getTask_latitude());
            cv.put(BDTasksManager.ColumnTasks.TASK_LONGITUDE, t.getTask_longitude());
            cv.put(BDTasksManager.ColumnTasks.TASK_STATUS, t.getTask_status());
            cv.put(BDTasksManager.ColumnTasks.TASK_USER_ID, t.getTask_user_id());

            bd.insert(BDTasksManager.TASKS_TABLE_NAME, null, cv);
            bd.close();

            Log.i("SQLite: ", "Add task in the bd with task_id :" + t.getTask_id());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTaskDetail(Context context, Integer task, String comment
            ,Integer status,Integer user,FilesManager encodedFile,Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTaskDetails.TASK_ID, task);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_STATUS,status);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_USER_ID, user);
            cv.put(BDTasksManager.ColumnTaskDetails.SERVER_SYNC, (serverSync)
                    ? Constants.SERVER_SYNC_TRUE : Constants.SERVER_SYNC_FALSE);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_COMMENT, comment);

            bd.insert(BDTasksManager.TASK_DETAILS_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_detail in the bd with task_id :"
                    + task + " task_comment : " + comment);

            Integer task_detail_cve = getLastTaskDetailCve(context,task);

            List<String> encodedVideoFiles = encodedFile.getEncodeVideoFiles();
            List<String> encodedPictureFiles = encodedFile.getEncodePictureFiles();


            for (String encode : encodedPictureFiles) {
                addTaskFiles(context, task_detail_cve, encode,Constants.PICTURE_FILE_TYPE);
            }

            /*for (String encode : encodedVideoFiles) {
                addTaskFiles(context, task_detail_cve, encode,Constants.VIDEO_FILE_TYPE);
            }*/

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTaskFiles(Context context, Integer detail_cve,String encodedFile
            , Integer fileType)
            throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE, detail_cve);
            cv.put(BDTasksManager.ColumnTasksFiles.BASE_FILE,encodedFile);
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_TYPE,fileType);

            bd.insert(BDTasksManager.TASKS_FILES_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_file in the bd with task_id :" + detail_cve);

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addUser(Context context,Users u) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnUsers.USER_ID, u.getIdUser());
            cv.put(BDTasksManager.ColumnUsers.USERNAME,u.getUserName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_ID,u.getIdActor());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME, u.getActorName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE, u.getActorType());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE_NAME,u.getActorTypeName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_ID,u.getIdTeam());
            cv.put(BDTasksManager.ColumnUsers.TEAM_NAME,u.getTeamName());
            cv.put(BDTasksManager.ColumnUsers.LATITUDE,u.getLatitude());
            cv.put(BDTasksManager.ColumnUsers.LONGITUDE,u.getLongitude());
            cv.put(BDTasksManager.ColumnUsers.LAST_TEAM_CONNECTION,u.getLastTeamConnection());
            cv.put(BDTasksManager.ColumnUsers.PASSWORD,u.getPassword());

            bd.insert(BDTasksManager.USERS_TABLE_NAME, null, cv);
            bd.close();

            Log.i("SQLite: ", "Add user in the bd with user_id : " + u.getIdUser());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static Tasks getTaskById(Context context, Tasks t) throws Exception {
        Tasks data = new Tasks();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from tasks where task_id =" + t.getTask_id(), null);

            if (result.moveToFirst()) {
                do {
                    data.setTask_cve(result.getInt(0));
                    data.setTask_tittle(result.getString(1));
                    data.setTask_content(result.getString(2));
                    data.setTask_priority(result.getInt(3));
                    data.setTask_begin_date(result.getString(4));
                    data.setTask_end_date(result.getString(5));
                    data.setTask_id(result.getInt(6));
                    data.setTask_latitude(result.getDouble(7));
                    data.setTask_longitude(result.getDouble(8));
                    data.setTask_status(result.getInt(9));
                    data.setTask_user_id(result.getInt(10));

                    Log.i("SQLite: ", "Get task in the bd with task_id :" + data.getTask_cve());
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static Integer getLastTaskDetailCve(Context context, Integer task_id) throws Exception {
        Integer data = 0;
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select max(task_detail_cve) from task_details where task_id =" + task_id
                    + " order by 1 desc", null);

            if (result.moveToFirst()) {
                do {
                    data = result.getInt(0);

                    Log.i("SQLite: ", "Get task_detail_cve in the bd with task_detail_cve :" + data);
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static  List<SyncTaskServer> getAllSyncTaskServer(Context context, Integer user_id, Integer server_sync) throws Exception {
        List<SyncTaskServer> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from task_details where task_user_id =" + user_id
                    + " and server_sync = " + server_sync
                    + " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {
                    SyncTaskServer sync = new SyncTaskServer();

                    sync.setTask_detail_cve(result.getInt(0));
                    sync.setTask_id(result.getInt(1));
                    sync.setTask_status(result.getInt(2));
                    sync.setTask_user_id(result.getInt(3));
                    sync.setServer_sync(result.getInt(4));
                    sync.setSendPictureFiles(getPictureFiles(context, sync.getTask_detail_cve()));
                    sync.setSendVideoFiles(getVideoFiles(context, sync.getTask_detail_cve()));

                    data.add(sync);

                    Log.i("SQLite: ", "Get task_details in the bd with task_user_id :" + user_id);
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static  List<String> getPictureFiles(Context context, Integer task_detail_cve) throws Exception {
        List<String> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.TASKS_FILES_TABLE_NAME
                    + " where " + BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE + " = " + task_detail_cve
                    + " and " + BDTasksManager.ColumnTasksFiles.FILE_TYPE + " = " + Constants.PICTURE_FILE_TYPE
                    + " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {

                    String file = result.getString(2);
                    data.add(file);

                    Log.i("SQLite: ", "Get task_file in the bd with task_detail_cve :" + task_detail_cve);
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static  List<String> getVideoFiles(Context context, Integer task_detail_cve) throws Exception {
        List<String> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.TASKS_FILES_TABLE_NAME
                    + " where " + BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE + " = " + task_detail_cve
                    + " and " + BDTasksManager.ColumnTasksFiles.FILE_TYPE + " = " + Constants.VIDEO_FILE_TYPE
                    + " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {

                    String file = result.getString(2);
                    data.add(file);

                    Log.i("SQLite: ", "Get task_file in the bd with task_detail_cve :" + task_detail_cve);
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static Users getUserById(Context context,Users u) throws Exception {
        Users data = new Users();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName,null,BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from users where idUser=" + u.getIdUser(),null);

            if (result.moveToFirst()) {
                do {

                    data.setCve_user(result.getInt(0));
                    data.setIdUser(result.getInt(1));
                    data.setUserName(result.getString(2));
                    data.setIdActor(result.getInt(3));
                    data.setActorName(result.getString(4));
                    data.setActorType(result.getInt(5));
                    data.setActorTypeName(result.getString(6));
                    data.setIdTeam(result.getInt(7));
                    data.setTeamName(result.getString(8));
                    data.setLatitude(result.getDouble(9));
                    data.setLongitude(result.getDouble(10));
                    data.setLastTeamConnection(result.getString(11));
                    data.setPassword(result.getString(12));

                    Log.i("SQLite: ", "Get user in the bd with idUser :" + data.getIdUser());
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
        return data;
    }

    public static Users getUserByCredentials(Context context,Users u) throws Exception {
        Users data = new Users();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName,null,BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from users where userName = '" + u.getUserName()
                    + "'",null);

            if (result.moveToFirst()) {
                do {

                    data.setCve_user(result.getInt(0));
                    data.setIdUser(result.getInt(1));
                    data.setUserName(result.getString(2));
                    data.setIdActor(result.getInt(3));
                    data.setActorName(result.getString(4));
                    data.setActorType(result.getInt(5));
                    data.setActorTypeName(result.getString(6));
                    data.setIdTeam(result.getInt(7));
                    data.setTeamName(result.getString(8));
                    data.setLatitude(result.getDouble(9));
                    data.setLongitude(result.getDouble(10));
                    data.setLastTeamConnection(result.getString(11));
                    data.setPassword(result.getString(12));

                    Log.i("SQLite: ", "Get user in the bd with idUser :" + data.getIdUser()
                    + " username : " + data.getUserName() + " password :" + data.getPassword());
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
        return data;
    }

    public static List<Tasks> getListTaskByStatus(Context context, Tasks t) throws Exception {
        List<Tasks> dataList = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from tasks where task_status=" + t.getTask_status(), null);

            if (result.moveToFirst()) {
                do {
                    Tasks data = new Tasks();

                    data.setTask_cve(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasks.TASK_CVE)));
                    data.setTask_tittle(result.getString(1));
                    data.setTask_content(result.getString(2));
                    data.setTask_priority(result.getInt(3));
                    data.setTask_begin_date(result.getString(4));
                    data.setTask_end_date(result.getString(5));
                    data.setTask_id(result.getInt(6));
                    data.setTask_latitude(result.getDouble(7));
                    data.setTask_longitude(result.getDouble(8));
                    data.setTask_status(result.getInt(9));
                    data.setTask_user_id(result.getInt(10));


                    dataList.add(data);

                    Log.i("SQLite: ", "Get task in the bd with task_id :" + data.getTask_id());
                } while(result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception","Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return dataList;
    }

    public static void updateCommonTask(Context context, Integer task, String comment
            ,Integer status,Integer user,FilesManager encodedFile,Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasks.TASK_STATUS,status);
            cv.put(BDTasksManager.ColumnTasks.TASK_USER_ID,user);

            bd.update(BDTasksManager.TASKS_TABLE_NAME, cv, BDTasksManager.ColumnTasks.TASK_ID + " = " + task, null);

            Log.i("SQLite: ", "Update task in the bd with task_id : " + task);

            addTaskDetail(context, task, comment, status, user, encodedFile, serverSync);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void updateUser(Context context, Users user) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnUsers.PASSWORD, user.getPassword());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME,user.getActorName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE,user.getActorType());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME,user.getActorTypeName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_NAME,user.getTeamName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_ID,user.getIdTeam());
            cv.put(BDTasksManager.ColumnUsers.LAST_TEAM_CONNECTION, user.getLastTeamConnection());

            bd.update(BDTasksManager.USERS_TABLE_NAME, cv, BDTasksManager.ColumnUsers.USER_ID + " = " + user.getIdUser(), null);
            bd.close();

            Log.i("SQLite: ", "Update user in the bd with user_id : " + user.getIdUser());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void updateTaskDetail(Context context, Integer task_detail_cve, Integer server_sync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTaskDetails.SERVER_SYNC, server_sync);

            bd.update(BDTasksManager.TASK_DETAILS_TABLE_NAME, cv,
                    BDTasksManager.ColumnTaskDetails.TASK_DETAIL_CVE + " = " + task_detail_cve, null);
            bd.close();

            Log.i("SQLite: ", "Update task_detail in the bd with task_detail_cve : " + task_detail_cve);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void cleanTables(Context context) throws  Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            bd.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASKS_TABLE_NAME);
            bd.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASK_DETAILS_TABLE_NAME);
            bd.execSQL(BDTasksManager.DROP_TABLE_IF_EXISTS + BDTasksManager.TASKS_FILES_TABLE_NAME);

            bd.execSQL(BDTasksManager.CREATE_TASKS_TABLE_SCRIPT);
            bd.execSQL(BDTasksManager.CREATE_TASK_DETAILS_TABLE_SCRIPT);
            bd.execSQL(BDTasksManager.CREATE_TASKS_FILES_TABLE_SCRIPT);

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }
}
