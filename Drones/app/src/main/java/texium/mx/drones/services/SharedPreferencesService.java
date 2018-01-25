package texium.mx.drones.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.utils.Constants;

/**
 * Created by jvier on 07/12/2017.
 */

public class SharedPreferencesService {

    public static TasksDecode getLocalizacion(Context context) {
        TasksDecode tasksDecode = new TasksDecode();

        SharedPreferences location = context.getSharedPreferences(Constants.KEY_PREF_LOCATION, Context.MODE_PRIVATE);
        String latitud = location.getString(Constants.KEY_MAIN_LATITUD, "");
        String longitud = location.getString(Constants.KEY_MAIN_LONGITUD, "");

        tasksDecode.setTask_latitude(latitud);
        tasksDecode.setTask_longitude(longitud);

        return tasksDecode;
    }

    public static void saveSessionPreferences(Context context, Location location) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.KEY_PREF_LOCATION, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.KEY_MAIN_LATITUD, String.valueOf(location.getLatitude()));
        editor.putString(Constants.KEY_MAIN_LONGITUD, String.valueOf(location.getLongitude()));
        editor.putBoolean(Constants.KEY_MAIN_RECORDAR, true);
        editor.commit();
    }
}
