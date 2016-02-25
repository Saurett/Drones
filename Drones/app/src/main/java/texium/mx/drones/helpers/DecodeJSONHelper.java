package texium.mx.drones.helpers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 25/02/2016.
 */
public class DecodeJSONHelper {

    public static TasksDecode decodeTask(String jsonData) {
        TasksDecode dataDecode = new TasksDecode();

        try {

            JSONObject jObject = new JSONObject(jsonData);

            dataDecode.setTask_type(jObject.getInt(Constants.JSON_DATA_ACCESS_TASK_TYPE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataDecode;
    }
}
