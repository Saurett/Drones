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
    static Integer BDVersion = 5;

    public static void addTasks(Context context,Tasks t) throws Exception {
        try{
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cvCloseTask = new ContentValues();

            cvCloseTask.put("task_title", t.getTask_tittle());
            cvCloseTask.put("task_content",t.getTask_content());
            cvCloseTask.put("task_priority",t.getTask_priority());
            cvCloseTask.put("task_begin_date",t.getTask_end_date());
            cvCloseTask.put("task_end_date", t.getTask_end_date());
            cvCloseTask.put("task_id",t.getTask_id());
            cvCloseTask.put("task_latitude",t.getTask_latitude());
            cvCloseTask.put("task_longitude",t.getTask_longitude());
            cvCloseTask.put("task_status",t.getTask_status());
            cvCloseTask.put("task_user_id",t.getTask_user_id());

            bd.insert("Tasks", null, cvCloseTask);
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

            ContentValues cvCloseTask = new ContentValues();

            cvCloseTask.put("idUser", u.getIdUser());
            cvCloseTask.put("userName",u.getUserName());
            cvCloseTask.put("idActor",u.getIdActor());
            cvCloseTask.put("actorName", u.getActorName());
            cvCloseTask.put("actorType", u.getActorType());
            cvCloseTask.put("actorTypeName",u.getActorTypeName());
            cvCloseTask.put("idTeam",u.getIdTeam());
            cvCloseTask.put("teamName",u.getIdTeam());
            cvCloseTask.put("latitude",u.getLatitude());
            cvCloseTask.put("longitude",u.getLongitude());
            cvCloseTask.put("lastTeamConnection",u.getLastTeamConnection());

            bd.insert("Users", null, cvCloseTask);
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

            Cursor result = bd.rawQuery("select * from users where user_id=" + u.getIdUser(),null);

            if (result.moveToFirst()) {
                do {

                    data.setCve_user(result.getInt(0));

                    Log.i("SQLite: ", "Get user in the bd with user_id :" + data.getIdUser());
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
        try{
            BDTasksManager bdTasksManager = new BDTasksManager(context,BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from tasks where  task_status=" + t.getTask_status(), null);

            if (result.moveToFirst()) {
                do {
                    Tasks tempTask = new Tasks();

                    tempTask.setTask_id(result.getInt(0));

                    dataList.add(tempTask);

                    Log.i("SQLite: ", "Get task in the bd with task_id :" + tempTask.getTask_id());
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
