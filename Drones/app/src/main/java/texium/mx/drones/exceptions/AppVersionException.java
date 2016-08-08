package texium.mx.drones.exceptions;

/**
 * Created by saurett on 12/04/2016.
 */
public class AppVersionException extends  Exception {

    public AppVersionException(String message) {
        super(message);
    }

    public AppVersionException(String message , Throwable cause) {
        super(message,cause);
    }
}
