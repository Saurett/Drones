package texium.mx.drones.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by saurett on 21/06/2016.
 */
public class DateTimeUtils {

    public static String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }
}
