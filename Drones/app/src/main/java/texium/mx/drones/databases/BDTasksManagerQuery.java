package texium.mx.drones.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.models.AppVersion;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.SyncTaskServer;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.utils.Constants;
import texium.mx.drones.utils.DateTimeUtils;

/**
 * Created by texiumuser on 17/03/2016.
 */
public class BDTasksManagerQuery {

    static String BDName = "BDTasksManager";
    static Integer BDVersion = 36;

    public static String getServer(Context context) throws Exception {
        String data = "";
        String tempURI = Constants.WEB_SERVICE_URL;

        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from links where link_status = " + Constants.ACTIVE, null);

            if (result.moveToFirst()) {
                do {

                    data = result.getString(result.getColumnIndex(BDTasksManager.ColumnLinks.LINK));

                    Log.i("SQLite: ", "Get link in the bd :" + data);
                } while (result.moveToNext());
            }

            bd.close();

            if (data.isEmpty()) {
                data = Constants.WEB_SERVICE_COMPLETE_URL;
            } else {
                data += tempURI;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static AppVersion getAppVersion(Context context) throws Exception {
        AppVersion appVersion = new AppVersion();
        String data = "";

        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from app_version where app_version_cve = 1", null);

            if (result.moveToFirst()) {
                do {

                    appVersion.setApp_version_cve(result.getInt(result.getColumnIndex(BDTasksManager.ColumnAppVersion.APP_VERSION_CVE)));
                    appVersion.setApp_version(result.getString(result.getColumnIndex(BDTasksManager.ColumnAppVersion.APP_VERSION)));
                    appVersion.setVersion_msg(result.getString(result.getColumnIndex(BDTasksManager.ColumnAppVersion.VERSION_MSG)));

                    Log.i("SQLite: ", "Get link in the bd :" + data);

                    Log.i("SQLite: ", "cve :" + appVersion.getApp_version_cve());
                    Log.i("SQLite: ", "app version :" + appVersion.getApp_version());
                    Log.i("SQLite: ", "msg :" + appVersion.getVersion_msg());

                    updateAppVersion(context, Constants.APP_VERSION, appVersion.getVersion_msg());

                } while (result.moveToNext());
            }

            bd.close();

            if (appVersion.getApp_version_cve() == null) {
                addAppVersion(context, Constants.APP_VERSION, "Si");

                appVersion.setApp_version_cve(1);
                appVersion.setApp_version(Constants.APP_VERSION);
                appVersion.setVersion_msg("Si");
            }

            Log.i("SQLite: ", "cve 2:" + appVersion.getApp_version_cve());
            Log.i("SQLite: ", "app version 2 :" + appVersion.getApp_version());
            Log.i("SQLite: ", "msg 2 :" + appVersion.getVersion_msg());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return appVersion;
    }

    public static void addAppVersion(Context context, String appVersion, String msg) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnAppVersion.APP_VERSION, appVersion);
            cv.put(BDTasksManager.ColumnAppVersion.VERSION_MSG, msg);

            bd.insert(BDTasksManager.APP_VERSION_TABLE_NAME, null, cv);
            bd.close();

            Log.i("SQLite: ", "Add app_version in the bd with");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }


    public static void updateAppVersion(Context context, String appVersion, String msg) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnAppVersion.APP_VERSION, appVersion);
            cv.put(BDTasksManager.ColumnAppVersion.VERSION_MSG, msg);

            bd.update(BDTasksManager.APP_VERSION_TABLE_NAME, cv, BDTasksManager.ColumnAppVersion.APP_VERSION_CVE + " = " + 1, null);

            Log.i("SQLite: ", "Update app_version in the bd with link : " + 1);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static Integer getCveLink(Context context) throws Exception {
        Integer data = null;

        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from links where link_status = " + Constants.ACTIVE, null);

            if (result.moveToFirst()) {
                do {

                    data = result.getInt(result.getColumnIndex(BDTasksManager.ColumnLinks.LINKS_CVE));

                    Log.i("SQLite: ", "Get link in the bd :" + data);
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static String getPartialServer(Context context) throws Exception {
        String data = "";

        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from Links where link_status = " + Constants.ACTIVE, null);

            if (result.moveToFirst()) {
                do {

                    data = result.getString(result.getColumnIndex(BDTasksManager.ColumnLinks.LINK));

                    Log.i("SQLite: ", "Get link in the bd :" + data);
                } while (result.moveToNext());
            }

            bd.close();

            if (data.isEmpty()) {
                data = Constants.WEB_SERVICE_PARTIAL_URL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static void addLink(Context context, String tempLink, Users user) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnLinks.LINK, tempLink);
            cv.put(BDTasksManager.ColumnLinks.USER_ID, user.getIdUser());
            cv.put(BDTasksManager.ColumnLinks.CREATION_DATE, DateTimeUtils.getActualTime());
            cv.put(BDTasksManager.ColumnLinks.LINK_STATUS, Constants.ACTIVE);

            bd.insert(BDTasksManager.LINKS_TABLE_NAME, null, cv);
            bd.close();

            Log.i("SQLite: ", "Add link in the bd with link name :" + tempLink);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void updateLink(Context context, int cveLink, Users user) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnLinks.LINK_STATUS, Constants.INACTIVE);
            cv.put(BDTasksManager.ColumnLinks.USER_ID, user.getIdUser());
            cv.put(BDTasksManager.ColumnLinks.UPDATE_DATE, DateTimeUtils.getActualTime());


            bd.update(BDTasksManager.LINKS_TABLE_NAME, cv, BDTasksManager.ColumnLinks.LINKS_CVE + " = " + cveLink, null);

            Log.i("SQLite: ", "Update link in the bd with link : " + cveLink);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTask(Context context, Tasks t) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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
            , Integer status, Integer user, FilesManager encodedFile, Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTaskDetails.TASK_ID, task);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_STATUS, status);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_USER_ID, user);
            cv.put(BDTasksManager.ColumnTaskDetails.SERVER_SYNC, (serverSync)
                    ? Constants.SERVER_SYNC_TRUE : Constants.SERVER_SYNC_FALSE);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_COMMENT, comment);

            bd.insert(BDTasksManager.TASK_DETAILS_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_detail in the bd with task_id :"
                    + task + " task_comment : " + comment);

            Integer task_detail_cve = getLastTaskDetailCve(context, task);

            List<String> encodedPictureFiles = encodedFile.getEncodePictureFiles();
            String encodedVideoFile = encodedFile.getEncodeVideoSingleFiles();


            for (String encode : encodedPictureFiles) {
                TaskGallery pg = new TaskGallery();

                pg.setFile_type(Constants.PICTURE_FILE_TYPE);
                pg.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
                pg.setBase_package(encode);
                pg.setDescription(Constants.EMPTY_STRING);
                pg.setCve_Task_Detail(task_detail_cve);

                addTaskFiles(context, pg);
            }

            TaskGallery pg = new TaskGallery();

            pg.setFile_type(Constants.VIDEO_FILE_TYPE);
            pg.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
            pg.setBase_package(encodedVideoFile);
            pg.setDescription(Constants.EMPTY_STRING);
            pg.setCve_Task_Detail(task_detail_cve);

            addTaskFiles(context, pg);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTaskDetailVideo(Context context, Integer task, String comment
            , Integer status, Integer user, FilesManager encodedFile, String videoBaseBitmap, Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTaskDetails.TASK_ID, task);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_STATUS, status);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_USER_ID, user);
            cv.put(BDTasksManager.ColumnTaskDetails.SERVER_SYNC, (serverSync)
                    ? Constants.SERVER_SYNC_TRUE : Constants.SERVER_SYNC_FALSE);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_COMMENT, comment);

            bd.insert(BDTasksManager.TASK_DETAILS_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_detail in the bd with task_id :"
                    + task + " task_comment : " + comment);

            Integer task_detail_cve = getLastTaskDetailCve(context, task);

            TaskGallery pg = new TaskGallery();

            pg.setFile_type(Constants.VIDEO_FILE_TYPE);
            pg.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
            pg.setBase_package(videoBaseBitmap);
            pg.setDescription(Constants.EMPTY_STRING);
            pg.setCve_Task_Detail(task_detail_cve);

            addTaskFilesVideo(context, pg, encodedFile);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTaskDetailPhotoGallery(Context context, Integer task, String comment
            , Integer status, Integer user, FilesManager encodedFile, Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTaskDetails.TASK_ID, task);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_STATUS, status);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_USER_ID, user);
            cv.put(BDTasksManager.ColumnTaskDetails.SERVER_SYNC, (serverSync)
                    ? Constants.SERVER_SYNC_TRUE : Constants.SERVER_SYNC_FALSE);
            cv.put(BDTasksManager.ColumnTaskDetails.TASK_COMMENT, comment);

            bd.insert(BDTasksManager.TASK_DETAILS_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_detail in the bd with task_id :"
                    + task + " task_comment : " + comment);

            Integer task_detail_cve = getLastTaskDetailCve(context, task);

            List<TaskGallery> photoGalleries = encodedFile.getTaskGalleries();


            for (TaskGallery taskGallery : photoGalleries) {
                TaskGallery pg = new TaskGallery();

                Integer syncType = (taskGallery.getSync_type().equals(Constants.ITEM_SYNC_SERVER_DEFAULT))
                        ? Constants.ITEM_SYNC_SERVER_DEFAULT : Constants.ITEM_SYNC_SERVER_CLOUD;

                pg.setFile_type(taskGallery.getFile_type());
                pg.setSync_type(syncType);
                pg.setBase_package(taskGallery.getBase_package());
                pg.setDescription(taskGallery.getDescription());
                pg.setCve_Task_Detail(task_detail_cve);
                pg.setId(taskGallery.getId());

                addTaskFiles(context, pg);
            }

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static TaskGallery getFileByServerId(Context context, TaskGallery photo) throws Exception {
        TaskGallery data = new TaskGallery();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.TASKS_FILES_TABLE_NAME +
                    " where " + BDTasksManager.ColumnTasksFiles.TASK_FILE_ID + " = " + photo.getId(), null);

            if (result.moveToFirst()) {
                do {

                    data.setCve(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_FILE_CVE)));
                    data.setId(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_FILE_ID)));
                    data.setCve_Task_Detail(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE)));
                    data.setBase_package(result.getString(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.BASE_FILE)));
                    data.setFile_type(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.FILE_TYPE)));
                    data.setSync_type(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.SERVER_SYNC)));
                    data.setDescription(result.getString(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE)));

                    Log.i("SQLite: ", "Get file in the bd ");
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static void addTaskFiles(Context context, TaskGallery gallery)
            throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            Integer fileStatus = (gallery.getSync_type().equals(Constants.ITEM_SYNC_SERVER_DELETE)
                    ? Constants.INACTIVE : Constants.ACTIVE);

            cv.put(BDTasksManager.ColumnTasksFiles.TASK_FILE_ID, gallery.getId());
            cv.put(BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE, gallery.getCve_Task_Detail());
            cv.put(BDTasksManager.ColumnTasksFiles.BASE_FILE, gallery.getBase_package());
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_TYPE, gallery.getFile_type());
            cv.put(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE, gallery.getDescription());
            cv.put(BDTasksManager.ColumnTasksFiles.SERVER_SYNC, gallery.getSync_type());
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_STATUS, fileStatus);

            bd.insert(BDTasksManager.TASKS_FILES_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add task_file in the bd with task_id ");

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addTaskFilesVideo(Context context, TaskGallery gallery, FilesManager encodedVideo)
            throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            Integer fileStatus = (gallery.getSync_type().equals(Constants.ITEM_SYNC_SERVER_DELETE)
                    ? Constants.INACTIVE : Constants.ACTIVE);

            cv.put(BDTasksManager.ColumnTasksFiles.TASK_FILE_ID, gallery.getId());
            cv.put(BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE, gallery.getCve_Task_Detail());
            cv.put(BDTasksManager.ColumnTasksFiles.BASE_FILE, gallery.getBase_package());
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_TYPE, gallery.getFile_type());
            cv.put(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE, gallery.getDescription());
            cv.put(BDTasksManager.ColumnTasksFiles.SERVER_SYNC, gallery.getSync_type());
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_STATUS, fileStatus);

            bd.insert(BDTasksManager.TASKS_FILES_TABLE_NAME, null, cv);

            List<String> packages = FileServices.getPackageList(context,encodedVideo.getEncodeVideoSingleFiles());

            for (String pack : packages) {
                addPackage(context, gallery, pack);
            }

            Log.i("SQLite: ", "Add task_file in the bd with task_id ");

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addPackage(Context context, TaskGallery gallery, String encodedVideo)
            throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            Integer cveFile = getLastTaskFile(context);

            //Integer fileStatus = (gallery.getSync_type().equals(Constants.ITEM_SYNC_SERVER_DELETE)
            //? Constants.INACTIVE : Constants.ACTIVE);

            cv.put(BDTasksManager.ColumnFilePackages.TEMP_KEY_ID, cveFile);
            cv.put(BDTasksManager.ColumnFilePackages.BASE_PACKAGE, encodedVideo);
            //cv.put(BDTasksManager.ColumnFilePackages.BASE_PACKAGE, "");
            cv.put(BDTasksManager.ColumnFilePackages.FILE_TYPE, gallery.getFile_type());

            bd.insert(BDTasksManager.FILES_PACKAGES_TABLE_NAME, null, cv);

            Log.i("SQLite: ", "Add package");

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static List<String> getPackages(Context context, TaskGallery photo) throws Exception {
        List<String> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.FILES_PACKAGES_TABLE_NAME +
                    " where " + BDTasksManager.ColumnFilePackages.TEMP_KEY_ID + " = " + photo.getCve() +
                    " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {

                    data.add(result.getString(1));

                    Log.i("SQLite: ", "Get file in the bd ");
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static void updateTaskFile(Context context, TaskGallery gallery) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();


            Integer fileStatus = (gallery.getSync_type().equals(Constants.ITEM_SYNC_SERVER_DELETE)
                    ? Constants.INACTIVE : Constants.ACTIVE);

            cv.put(BDTasksManager.ColumnTasksFiles.SERVER_SYNC, gallery.getSync_type());
            cv.put(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE, gallery.getDescription());
            cv.put(BDTasksManager.ColumnTasksFiles.TASK_FILE_ID, gallery.getId());
            cv.put(BDTasksManager.ColumnTasksFiles.FILE_STATUS, fileStatus);

            bd.update(BDTasksManager.TASKS_FILES_TABLE_NAME, cv,
                    BDTasksManager.ColumnTasksFiles.TASK_FILE_CVE + " = " + gallery.getCve(), null);
            bd.close();

            Log.i("SQLite: ", "Update task_file in the bd ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void deleteTaskFile(Context context, TaskGallery gallery) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasksFiles.SERVER_SYNC, gallery.getSync_type());
            cv.put(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE, gallery.getDescription());

            bd.delete(BDTasksManager.TASKS_FILES_TABLE_NAME,
                    BDTasksManager.ColumnTasksFiles.TASK_FILE_CVE + " = " + gallery.getCve(), null);
            bd.close();

            Log.i("SQLite: ", "Update task_file in the bd ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void addUser(Context context, Users u) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnUsers.USER_ID, u.getIdUser());
            cv.put(BDTasksManager.ColumnUsers.USERNAME, u.getUserName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_ID, u.getIdActor());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME, u.getActorName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE, u.getActorType());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE_NAME, u.getActorTypeName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_ID, u.getIdTeam());
            cv.put(BDTasksManager.ColumnUsers.TEAM_NAME, u.getTeamName());
            cv.put(BDTasksManager.ColumnUsers.LATITUDE, u.getLatitude());
            cv.put(BDTasksManager.ColumnUsers.LONGITUDE, u.getLongitude());
            cv.put(BDTasksManager.ColumnUsers.LAST_TEAM_CONNECTION, u.getLastTeamConnection());
            cv.put(BDTasksManager.ColumnUsers.PASSWORD, u.getPassword());

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
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static Integer getLastTaskDetailCve(Context context, Integer task_id) throws Exception {
        Integer data = 0;
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select max(task_detail_cve) from task_details where task_id =" + task_id
                    + " order by 1 desc", null);

            if (result.moveToFirst()) {
                do {
                    data = result.getInt(0);

                    Log.i("SQLite: ", "Get task_detail_cve in the bd with task_detail_cve :" + data);
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static Integer getLastTaskFile(Context context) throws Exception {
        Integer data = 0;
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select max(" + BDTasksManager.ColumnTasksFiles.TASK_FILE_CVE + ") from "
                    + BDTasksManager.TASKS_FILES_TABLE_NAME
                    + " order by 1 desc", null);

            if (result.moveToFirst()) {
                do {
                    data = result.getInt(0);
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Database error");
        }

        return data;
    }

    public static List<SyncTaskServer> getAllSyncTaskServer(Context context, Integer user_id, Integer server_sync) throws Exception {
        List<SyncTaskServer> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from task_details "
                    + " where server_sync = " + server_sync
                    //+ " and task_user_id =" + user_id//
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
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static List<String> getPictureFiles(Context context, Integer task_detail_cve) throws Exception {
        List<String> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static List<TaskGallery> getGalleryFiles(Context context, List<Integer> task_detail_cve, Integer fileType, List<Integer> syncType, Integer fileStatus) throws Exception {
        List<TaskGallery> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            String queryCve = " where " + BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE + " in (" + task_detail_cve + " )";
            queryCve = queryCve.replace("[", "");
            queryCve = queryCve.replace("]", "");

            String querySync = (null != syncType) ? " and " + BDTasksManager.ColumnTasksFiles.SERVER_SYNC + " in (" + syncType + ")" : "";
            querySync = querySync.replace("[", "");
            querySync = querySync.replace("]", "");

            String queryFS = (null != fileStatus) ? " and " + BDTasksManager.ColumnTasksFiles.FILE_STATUS + " = " + fileStatus : "";

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.TASKS_FILES_TABLE_NAME
                    + queryCve + " and " + BDTasksManager.ColumnTasksFiles.FILE_TYPE + " = " + fileType
                    + queryFS + querySync + " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {

                    TaskGallery taskGallery = new TaskGallery();

                    taskGallery.setCve(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_FILE_CVE)));
                    taskGallery.setId(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_FILE_ID)));
                    taskGallery.setCve_Task_Detail(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.TASK_DETAIL_CVE)));
                    taskGallery.setBase_package(result.getString(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.BASE_FILE)));
                    taskGallery.setFile_type(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.FILE_TYPE)));
                    taskGallery.setSync_type(result.getInt(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.SERVER_SYNC)));
                    taskGallery.setDescription(result.getString(result.getColumnIndex(BDTasksManager.ColumnTasksFiles.DESCRIPTION_FILE)));


                    data.add(taskGallery);

                    Log.i("SQLite: ", "Get task_file in the bd with task_detail_cve :" + task_detail_cve);
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static List<String> getVideoFiles(Context context, Integer task_detail_cve) throws Exception {
        List<String> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }

    public static Users getUserById(Context context, Users u) throws Exception {
        Users data = new Users();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from users where idUser=" + u.getIdUser(), null);

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
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
        return data;
    }

    public static Users getUserByCredentials(Context context, Users u) throws Exception {
        Users data = new Users();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from users where userName = '" + u.getUserName()
                    + "'", null);

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
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
        return data;
    }

    public static List<Tasks> getListTaskByStatus(Context context, Tasks t) throws Exception {
        List<Tasks> dataList = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return dataList;
    }

    public static void updateCommonTask(Context context, Integer task, String comment
            , Integer status, Integer user, FilesManager encodedFile, Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasks.TASK_STATUS, status);
            cv.put(BDTasksManager.ColumnTasks.TASK_USER_ID, user);

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

    public static void updateCommonTaskVideo(Context context, Integer task, String comment
            , Integer status, Integer user, FilesManager encodedFile, Boolean serverSync) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnTasks.TASK_STATUS, status);
            cv.put(BDTasksManager.ColumnTasks.TASK_USER_ID, user);

            bd.update(BDTasksManager.TASKS_TABLE_NAME, cv, BDTasksManager.ColumnTasks.TASK_ID + " = " + task, null);

            Log.i("SQLite: ", "Update task in the bd with task_id : " + task);

            addTaskDetailVideo(context, task, comment, status, user, encodedFile, "", serverSync);

            bd.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }
    }

    public static void updateUser(Context context, Users user) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(BDTasksManager.ColumnUsers.PASSWORD, user.getPassword());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME, user.getActorName());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_TYPE, user.getActorType());
            cv.put(BDTasksManager.ColumnUsers.ACTOR_NAME, user.getActorTypeName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_NAME, user.getTeamName());
            cv.put(BDTasksManager.ColumnUsers.TEAM_ID, user.getIdTeam());
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
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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

    public static void cleanTables(Context context) throws Exception {
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
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

    public static List<Integer> getListTaskDetail(Context context, Integer taskID) throws Exception {
        List<Integer> data = new ArrayList<>();
        try {
            BDTasksManager bdTasksManager = new BDTasksManager(context, BDName, null, BDVersion);
            SQLiteDatabase bd = bdTasksManager.getWritableDatabase();

            Cursor result = bd.rawQuery("select * from " + BDTasksManager.TASK_DETAILS_TABLE_NAME
                    + " where " + BDTasksManager.ColumnTaskDetails.TASK_ID + " = " + taskID
                    + " order by 1 asc", null);

            if (result.moveToFirst()) {
                do {

                    data.add(result.getInt(0));

                    Log.i("SQLite: ", "Get task_details in the bd");
                } while (result.moveToNext());
            }

            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite Exception", "Database error: " + e.getMessage());
            throw new Exception("Database error");
        }

        return data;
    }
}
