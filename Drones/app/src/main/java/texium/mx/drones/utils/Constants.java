package texium.mx.drones.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by texiumuser on 25/02/2016.
 */
public final class Constants {



    //region SYS CONSTANTS//
    public static final String NUMBER_ZERO = "0";
    //endregion SYS CONSTANTS//

    //region DATABASE STATUS//
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int NEWS_TASK = 3;
    public static final int PROGRESS_TASK = 4;
    public static final int PENDING_TASK = 5;
    public static final int CLOSE_TASK = 6;
    @Deprecated  public static final int REVISION_TASK = 7;
    //endregion DATABASE STATUS

    //region MAP ACCESS//
    @Deprecated public static final String JSON_DATA_ACCESS_TASK_TYPE = "task_type";
    //endregion MAP ACCESS//

    //region TOKEN ACCESS//
    public static final Long TOKEN_KEY_ACCESS_TASK_VIEW = 1L;
    public static final Long TOKEN_KEY_ACCESS_TASK_ADAPTER = 2L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS = 3L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS_DECODE = 4L;
    //endregion TOKEN ACCESS//

    //region FRAGMENT TAGS//
    public static final String FRAGMENT_NEWS_TAG = "fragment_news_task";
    public static final String FRAGMENT_PROGRESS_TAG = "fragment_progress_task";
    public static final String FRAGMENT_PENDING_TAG = "fragment_pending_task";
    @Deprecated public static final String FRAGMENT_REVISION_TAG = "fragment_revision_task";
    public static final String FRAGMENT_CLOSE_TAG = "fragment_close_task";
    public static final String FRAGMENT_FINISH_TAG = "fragment_finish_task";
    //endregion FRAGMENT TAGS//

    //region WEB SERVICE ID //
    public static final int WS_KEY_PUBLIC_TEST = 0;
    public static final int WS_KEY_LOGIN_SERVICE = 1;
    public static final int WS_KEY_TASK_SERVICE_NEWS = 2;
    public static final int WS_KEY_TASK_SERVICE_PROGRESS = 3;
    public static final int WS_KEY_TASK_SERVICE_PENDING= 4;
    public static final int WS_KEY_UPDATE_TASK = 5;
    public static final int WS_KEY_SEND_LOCATION = 6;
    public static final int WS_KEY_SEND_LOCATION_HIDDEN = 7;
    @Deprecated public static final int WS_KEY_FIND_TASK = 8;
    @Deprecated public static final int WS_KEY_TASK_SERVICE_REVISION = 9; //ONLI WEB
    public static final int WS_KEY_TASK_SERVICE_CLOSE = 10;
    //endregion WEB SERVICE ID//

    //region SOAP CONFIGURATION//
    /**
     * NEVER CHANGE IP SERVICE_NAMESPACE
     *
     * TEXIUM NANAGER
     * *********************************
     *
     * WEB_SERVICE_URL = "http://192.168.12.111/SistemaMedioAmbienteDF/ServicioWeb/Servicio_Android.asmx";
     *
     * **********************************
     * SEDEMA MANAGER
     *
     * WEB_SERVICE_URL http://192.168.1.98/ServicioWeb/Servicio_Android.asmx
     *
     * **********************************
     *
     */
    public static final String WEB_SERVICE_SOAP_ACTION = "192.168.12.10/LoginMovil";
    public static final String WEB_SERVICE_SOAP_ACTION_TASK_LIST = "192.168.12.10/ListadoTareas";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_TASK = "192.168.12.10/ActualizarTarea";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_LOCATION = "192.168.12.10/SincronizarCoordenadas";
    public static final String WEB_SERVICE_SOAP_ACTION_SEND_FILE = "192.168.12.10/AgregarArchivos";
    public static final String WEB_SERVICE_NAMESPACE = "192.168.12.10";
    public static final String WEB_SERVICE_URL = "http://192.168.12.111/SistemaMedioAmbienteDF/ServicioWeb/Servicio_Android.asmx";
    //public static final String WEB_SERVICE_URL = "http://192.168.1.98/ServicioWeb/Servicio_Android.asmx";
    //endregion SOAP CONFIGURATION//

    //region SOAP OPERATION//
    public static final String WEB_SERVICE_METHOD_NAME_LOGIN = "LoginMovil";
    public static final String WEB_SERVICE_METHOD_NAME_TASK = "ListadoTareas";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_TASK = "ActualizarTarea";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_LOCATION = "SincronizarCoordenadas";
    public static final String WEB_SERVICE_METHOD_NAME_SEND_FILE= "AgregarArchivos";
    //endregion SOAP OPERATION//

    //region WEB SERVICE PARAMS//
    public static final String WEB_SERVICE_PARAM_LOGIN_USERNAME = "NombreUsuario";
    public static final String WEB_SERVICE_PARAM_LOGIN_PASSWORD = "ContrasenaUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_ID_TEAM = "IDCuadrilla";
    public static final String WEB_SERVICE_PARAM_TASK_STATUS = "IDEstatus";
    public static final String WEB_SERVICE_PARAM_TASK_ID = "IDTarea";
    public static final String WEB_SERVICE_PARAM_TASK_COMMENT = "Observacion";
    public static final String WEB_SERVICE_PARAM_TASK_ID_USER = "IDUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_LATITUDE = "Latitud";
    public static final String WEB_SERVICE_PARAM_TASK_LONGITUDE = "Longitud";
    //endregion WEB SERVICE PARAMS//

    //region Google Maps LOCATION//
    public static final Double GOOGLE_MAPS_LATITUDE = 19.4265606;
    public static final Double GOOGLE_MAPS_LONGITUDE = -99.0672223;
    public static final float GOOGLE_MAPS_DEFAULT_CAMERA = 10;
    //endregion Google Maps LOCATION//

    //region SOAP KEYS//
    public static final String SOAP_OBJECT_KEY_LOGIN_ID = "idusuario";
    public static final String SOAP_OBJECT_KEY_LOGIN_USERNAME = "nombreusuario";
    public static final String SOAP_OBJECT_KEY_LOGIN_ID_ACTOR = "idactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTOR_NAME = "nombreactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPE = "idtipoactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPENAME = "nombretipoactor";
    public static final String SOAP_OBJECT_KEY_LOGIN_ID_TEAM = "idcuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAM_NAME = "nombrecuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION = "coordenadascuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_LATITUDE = "Latitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LONGITUDE = "Longitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LAST_CONNECTION = "ultimaconexioncuadrilla";
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
    //endregion SOAP KEYS//

    //region ACTIVITY PARAMS//
    public static final String ACTIVITY_EXTRA_PARAMS_LOGIN = "data";
    //endregion ACTIVITY PARAMS//


    //region ACCESS TO MAP TASK PRIORITY//
    public static final Integer TASK_PRIORITY_LOW_ID = 1;
    public static final Integer TASK_PRIORITY_MID_ID = 2;
    public static final Integer TASK_PRIORITY_TOP_ID = 3;
    public static final Integer TASK_PRIORITY_EXPRESS_ID = 4;

    public static final String TASK_PRIORITY_LOW_STR = "Baja";
    public static final String TASK_PRIORITY_MID_STR = "Mediana";
    public static final String TASK_PRIORITY_TOP_STR = "Alta";
    public static final String TASK_PRIORITY_EXPRESS_STR = "Urgente";

    public static Map<Integer,String> MAP_STATUS_NAME;
    static {
        MAP_STATUS_NAME = new HashMap<>();
        MAP_STATUS_NAME.put(TASK_PRIORITY_LOW_ID,TASK_PRIORITY_LOW_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_MID_ID,TASK_PRIORITY_MID_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_TOP_ID,TASK_PRIORITY_TOP_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_EXPRESS_ID,TASK_PRIORITY_EXPRESS_STR);
    }

    //endregion ACCESS TO MAP PRIORITY//
}
