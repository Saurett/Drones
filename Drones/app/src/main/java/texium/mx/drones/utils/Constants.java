package texium.mx.drones.utils;

import java.util.HashMap;
import java.util.Map;

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

    @Deprecated
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

    @Deprecated
    public static final String FRAGMENT_REVISION_TAG = "fragment_revision_task";

    public static final String FRAGMENT_CLOSE_TAG = "fragment_close_task";
    public static final String FRAGMENT_FINISH_TAG = "fragment_finish_task";

    //WEB SERVICE ID //
    public static final int WS_KEY_PUBLIC_TEST = 0;
    public static final int WS_KEY_LOGIN_SERVICE = 1;
    public static final int WS_KEY_TASK_SERVICE_NEWS = 2;
    public static final int WS_KEY_TASK_SERVICE_PROGRESS = 3;
    public static final int WS_KEY_TASK_SERVICE_PENDING= 4;
    public static final int WS_KEY_UPDATE_TASK = 1;
    public static final int WS_KEY_SEND_LOCATION = 2;
    public static final int WS_KEY_SEND_LOCATION_HIDDEN = 3;
    public static final int WS_KEY_FIND_TASK = 4;

    @Deprecated
    public static final int WS_KEY_TASK_SERVICE_REVISION = 5; //ONLI WEB

    public static final int WS_KEY_TASK_SERVICE_CLOSE = 6;

    //WS SWITCH OPERATIONS//
    ;

    //SOAP CONFIGURATION//
    public static final String WEB_SERVICE_SOAP_ACTION = "192.168.12.10/LoginMovil";
    public static final String WEB_SERVICE_SOAP_ACTION_TASKLIST = "192.168.12.10/ListadoTareas";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_TASK = "192.168.12.10/ActualizarTarea";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_LOCATION = "192.168.12.10/SincronizarCoordenadas";
    public static final String WEB_SERVICE_NAMESPACE = "192.168.12.10";
    public static final String WEB_SERVICE_URL = "http://192.168.12.111/SistemaMedioAmbienteDF/ServicioWeb/Servicio_Android.asmx";

    //OPERERATION SOAP//
    public static final String WEB_SERVICE_METHOD_NAME_LOGIN = "LoginMovil";
    public static final String WEB_SERVICE_METHOD_NAME_TASK = "ListadoTareas";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_TASK = "ActualizarTarea";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_LOCATION = "SincronizarCoordenadas";

    //WEB SERVICE PARAMS//
    public static final String WEB_SERVICE_PARAM_LOGIN_USERNAME = "NombreUsuario";
    public static final String WEB_SERVICE_PARAM_LOGIN_PASSWORD = "ContrasenaUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_ID_TEAM = "IDCuadrilla";
    public static final String WEB_SERVICE_PARAM_TASK_STATUS = "IDEstatus";
    public static final String WEB_SERVICE_PARAM_TASK_ID = "IDTarea";
    public static final String WEB_SERVICE_PARAM_TASK_COMMENT = "Observacion";
    public static final String WEB_SERVICE_PARAM_TASK_ID_USER = "IDUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_LATITUDE = "Latitud";
    public static final String WEB_SERVICE_PARAM_TASK_LONGITUDE = "Longitud";

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

    public static final String SOAP_OBJECT_KEY_TASK_ID = "ID";
    public static final String SOAP_OBJECT_KEY_TASK_TITTLE = "Nombre";
    public static final String SOAP_OBJECT_KEY_TASK_CONTENT = "Descripcion";
    public static final String SOAP_OBJECT_KEY_TASK_LOCATION = "Coordenadas";
    public static final String SOAP_OBJECT_KEY_TASK_LATITUDE = "Latitud";
    public static final String SOAP_OBJECT_KEY_TASK_LONGITUDE = "Longitud";
    public static final String SOAP_OBJECT_KEY_TASK_PRIORITY = "IDPrioridad";
    public static final String SOAP_OBJECT_KEY_TASK_BEGIN_DATE = "FechaInicio";
    public static final String SOAP_OBJECT_KEY_TASK_END_DATE = "FechaFin";
    public static final String SOAP_OBJECT_KEY_TASK_STATUS = "IDEstatus";
    public static final String SOAP_OBJECT_KEY_TASK_USER_ID = "IDUsuario";

    //ACTIVY PARAMS//
    public static final String ACTIVITY_EXTRA_PARAMS_LOGIN = "data";


    //ACEESS TO MAP TASK PRIORITY//
    public static final Integer TASK_PRIORITY_LOW_ID = 1;

    public static final String TASK_PRIORITY_LOW_STR = "Baja";


    public static Map<Integer,String> MAP_STATUS_NAME;
    static {
        MAP_STATUS_NAME = new HashMap<>();
        MAP_STATUS_NAME.put(TASK_PRIORITY_LOW_ID,TASK_PRIORITY_LOW_STR);
        MAP_STATUS_NAME.put(2,"Media");
        MAP_STATUS_NAME.put(3,"Alta");
        MAP_STATUS_NAME.put(4,"Urgente");
    }

}
