package texium.mx.drones.utils;

/**
 * Created by texiumuser on 25/02/2016.
 */
public final class Constants {

    //DATA STATUS
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;

    //TASKS ID
    public static final int NEWS_TASK = 3;
    public static final int PROGRESS_TASK = 4;
    public static final int PENDING_TASK = 5;
    public static final int CLOSE_TASK = 6;
    public static final int REVISION_TASK = 7;

    //MAP ACCESS
    public static final String JSON_DATA_ACCESS_TASK_TYPE = "task_type";

    //TOKEN ACCESS
    public static final Long TOKEN_KEY_ACCESS_TASK_VIEW = 1L;
    public static final Long TOKEN_KEY_ACCESS_TASK_ADAPTER = 2L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS = 3L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS_DECODE = 4L;

    //Fragments tags
    public static final String FRAGMENT_NEWS_TAG = "fragment_news_task";
    public static final String FRAGMENT_PROGRESS_TAG = "fragment_progress_task";
    public static final String FRAGMENT_PENDING_TAG = "fragment_pending_task";
    public static final String FRAGMENT_REVISION_TAG = "fragment_revision_task";
    public static final String FRAGMENT_CLOSE_TAG = "fragment_close_task";
    public static final String FRAGMENT_FINISH_TAG = "fragment_finish_task";

    //WEB SERVICE ID //
    public static final int WS_KEY_PUBLIC_TEST = 0;
    public static final int WS_KEY_LOGIN_SERVICE = 1;
    public static final int WS_KEY_TASK_SERVICE_NEWS = 2;
    public static final int WS_KEY_TASK_SERVICE_PROGRESS = 3;
    public static final int WS_KEY_TASK_SERVICE_PENDING= 4;
    public static final int WS_KEY_TASK_SERVICE_REVISION = 5;
    public static final int WS_KEY_TASK_SERVICE_CLOSE = 6;

    //SOAP CONFIGURATION//
    public static final String WEB_SERVICE_SOAP_ACTION = "192.168.12.10/LoginMovil";
    public static final String WEB_SERVICE_NAMESPACE = "192.168.12.10";
    public static final String WEB_SERVICE_URL = "http://192.168.12.111/SistemaMedioAmbienteDF/ServicioWeb/Servicio_Android.asmx";

    //OPERERATION SOAP//
    public static final String WEB_SERVICE_METHOD_NAME_LOGIN = "LoginMovil";

    //WEB SERVICE PARAMS//
    public static final String WEB_SERVICE_PARAM_LOGIN_USERNAME = "NombreUsuario";
    public static final String WEB_SERVICE_PARAM_LOGIN_PASSWORD = "ContrasenaUsuario";

    //Google Maps COORD//
    public static final Double GOOGLE_MAPS_LATITUDE = 19.4265606;
    public static final Double GOOGLE_MAPS_LONGITUDE = -99.0672223;
    public static final float GOOGLE_MAPS_DEFAULT_CAMERA = 10;

    //SOAP KEYS//
    public static final String SOAP_OBJECT_KEY_LOGIN_ID = "idusuario";
    public static final String SOAP_OBJECT_KEY_LOGIN_USERNAME = "nombreusuario";
    public static final String SOAP_OBJECT_KEY_LOGIN_ID_ACTOR = "idactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTORNAME = "nombreactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPE = "idtipoactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPENAME = "nombretipoactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ID_TEAM = "idcuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAMNAME = "nombrecuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION = "coordenadascuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_LATITUDE = "Latitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LONGITUDE = "Longitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LAST_CONEXION = "ultimaconexioncuadrilla";

    //ACTIVY PARAMS//
    public static final String ACTIVITY_EXTRA_PARAMS_LOGIN = "data";






}
