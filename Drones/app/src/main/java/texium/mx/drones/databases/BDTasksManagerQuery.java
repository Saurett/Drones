package texium.mx.drones.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;

/**
 * Created by texiumuser on 17/03/2016.
 */
public class BDTasksManagerQuery {

    static String BDName = "BDTasksManager";
    static Integer BDVersion = 8;

    public static void addTask(Context context, Tasks t) throws Exception {
        try{
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put("task_title", t.getTask_tittle());
            cv.put("task_content", t.getTask_content());
            cv.put("task_priority", t.getTask_priority());
            cv.put("task_begin_date", t.getTask_end_date());
            cv.put("task_end_date", t.getTask_end_date());
            cv.put("task_id", t.getTask_id());
            cv.put("task_latitude", t.getTask_latitude());
            cv.put("task_longitude", t.getTask_longitude());
            cv.put("task_status", t.getTask_status());
            cv.put("task_user_id", t.getTask_user_id());

            bd.insert("Tasks", null, cv);
            bd.close();

            Log.i("SQLite: ","Add task in the bd with task_id :" + t.getTask_id());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addUser(Context context,Users u) throws Exception {
        try{
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put("idUser", u.getIdUser());
            cv.put("userName",u.getUserName());
            cv.put("idActor",u.getIdActor());
            cv.put("actorName", u.getActorName());
            cv.put("actorType", u.getActorType());
            cv.put("actorTypeName",u.getActorTypeName());
            cv.put("idTeam",u.getIdTeam());
            cv.put("teamName",u.getIdTeam());
            cv.put("latitude",u.getLatitude());
            cv.put("longitude",u.getLongitude());
            cv.put("lastTeamConnection",u.getLastTeamConnection());
            cv.put("password",u.getPassword());

            bd.insert("Users", null, cv);
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
        try{
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
                    data.setTask_id(result.getInt(result.getInt(6)));
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
                    + "' and password ='" + u.getPassword() +"'",null);

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

    public static List<Tasks> getListTaskByStatus(Context context, Tasks t) throws Exception {
        List<Tasks> dataList = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from tasks where task_status=" + t.getTask_status(), null);

            if (result.moveToFirst()) {
                do {
                    Tasks data = new Tasks();

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
}
