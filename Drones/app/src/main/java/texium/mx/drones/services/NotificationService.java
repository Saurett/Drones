package texium.mx.drones.services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import texium.mx.drones.R;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;

/**
 * Created by saurett on 20/09/2016.
 */

public class NotificationService {

    public static void taskNotification(Activity activity, Integer notificationID, String title, Tasks task) {

        Context context = activity.getApplicationContext();

        String contentText = "Prioridad " + Constants.MAP_STATUS_NAME.get(task.getTask_priority());
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_assignment_white, 1)
                .setAutoCancel(true)
                .setColor(activity.getResources().getColor(Constants.MAP_STATUS_COLOR.get(task.getTask_priority())))
                .setContentTitle(title)
                .setSound(uri)
                .setContentText(contentText);

        Users user = (Users) activity.getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

        Intent resultIntent = new Intent(context, activity.getClass());
        resultIntent.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_NEW_TASK, Constants.FRAGMENT_NEWS_TAG);
        resultIntent.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, user);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activity);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public static void callNotification(Activity activity, Integer idUser) {

        try {
            Context context = activity.getApplicationContext();

            SoapObject soapObject = SoapServices.getServerAllTasks(context, idUser, Constants.NEWS_TASK);

            if (soapObject.getPropertyCount() > 0) {

                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    Tasks t = new Tasks();

                    SoapObject soTemp = (SoapObject) soapObject.getProperty(i);
                    SoapObject soLocation = (SoapObject) soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LOCATION);

                    t.setTask_tittle(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_TITTLE).toString());
                    t.setTask_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                    t.setTask_content(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                    t.setTask_latitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LATITUDE).toString()));
                    t.setTask_longitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LONGITUDE).toString()));
                    t.setTask_priority(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_PRIORITY).toString()));
                    t.setTask_begin_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_BEGIN_DATE).toString());
                    t.setTask_end_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_END_DATE).toString());
                    t.setTask_status(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString()));
                    t.setTask_user_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID).toString()));

                    try {
                        Tasks tempTask = BDTasksManagerQuery.getTaskById(context, t);

                        Integer tempTaskStatus = (tempTask.getTask_id() != null)
                                ? tempTask.getTask_status() : Constants.INACTIVE;

                        switch (tempTaskStatus) {
                            case Constants.INACTIVE:
                                BDTasksManagerQuery.addTask(context, t);
                                NotificationService.taskNotification(activity, t.getTask_id(), "Tarea Nueva Asignada", t);
                                break;
                            default:
                                Log.i("NewsTasks", "No remove task");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("NewsTasksException: ", "Unknown error: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
