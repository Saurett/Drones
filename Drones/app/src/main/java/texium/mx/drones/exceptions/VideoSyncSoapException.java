package texium.mx.drones.exceptions;

/**
 * Created by saurett on 12/04/2016.
 */
public class VideoSyncSoapException extends  Exception {

    public VideoSyncSoapException(String message) {
        super(message);
    }

    public VideoSyncSoapException(String message , Throwable cause) {
        super(message,cause);
    }
}
