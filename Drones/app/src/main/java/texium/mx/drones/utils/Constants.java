package texium.mx.drones.utils;


import java.util.HashMap;
import java.util.Map;

import texium.mx.drones.R;

/**
 * Created by texiumuser on 25/02/2016.
 */
public final class Constants {

    public static final int LOOP_TIME = 180000; //60000 * 1 = 1 Minute
    public static final int VIDEO_SIZE = 43932160;  //X MB*1048*1048= X MB

    //region SYS CONSTANTS//
    //region SYS CONSTANTS//
    public static final String EMPTY_STRING = "";
    public static final String NUMBER_ZERO = "0";
    public static final Integer SERVER_SYNC_FALSE = 0;
    public static final Integer SERVER_SYNC_TRUE = 1;

    public static final int LOGIN_FORM = 1;
    public static final int FORGET_USERNAME_FORM = 2;
    public static final int CONNECTIVITY_FORM = 3;

    public static final int PICTURE_FILE_TYPE = 1;
    public static final int VIDEO_FILE_TYPE = 2;
    public static final int DOCUMENT_FILE_TYPE = 3;
    public static final int MEMBER_TYPE = 4;

    public static final int MESSAGE_ALERT_NO_FRAGMENT = -1;

    //endregion SYS CONSTANTS//

    //region DATABASE STATUS//
    public static final int ALL_TASK = 0;
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int NEWS_TASK = 3;
    public static final int PROGRESS_TASK = 4;
    public static final int PENDING_TASK = 5;
    public static final int CLOSE_TASK = 6;
    public static final int REVISION_TASK = 7;
    public static final int CANCEL_TASK = 8;
    //endregion DATABASE STATUS

    //region MAP ACCESS//
    @Deprecated
    public static final String JSON_DATA_ACCESS_TASK_TYPE = "task_type";
    //endregion MAP ACCESS//

    //region TOKEN ACCESS//
    public static final Long TOKEN_KEY_ACCESS_TASK_VIEW = 1L;
    public static final Long TOKEN_KEY_ACCESS_TASK_ADAPTER = 2L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS = 3L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS_DECODE = 4L;
    public static final Long TOKEN_KEY_ACCESS_FILE_MANAGER_CLASS = 5L;
    //endregion TOKEN ACCESS//

    //region FRAGMENT TAGS//
    public static final String FRAGMENT_NEWS_TAG = "fragment_news_task";
    public static final String FRAGMENT_PROGRESS_TAG = "fragment_progress_task";
    public static final String FRAGMENT_PENDING_TAG = "fragment_pending_task";
    @Deprecated
    public static final String FRAGMENT_REVISION_TAG = "fragment_revision_task";
    public static final String FRAGMENT_CLOSE_TAG = "fragment_close_task";
    public static final String FRAGMENT_FINISH_TAG = "fragment_finish_task";
    public static final String FRAGMENT_RESTORE_TAG = "fragment_restore";
    @Deprecated
    public static final String FRAGMENT_UPLOAD_FILE = "fragment_upload_file";
    public static final String FRAGMENT_PHOTO_GALLERY_LIST_TAG = "fragment_photo_gallery_list";
    public static final String FRAGMENT_VIDEO_GALLERY_LIST_TAG = "fragment_video_gallery_list";
    public static final String FRAGMENT_DOCUMENT_GALLERY_LIST_TAG = "fragment_document_gallery_list";
    public static final String FRAGMENT_MEMBER_GALLERY_LIST_TAG = "fragment_member_gallery_list";
    public static final String FRAGMENT_PHOTO_GALLERY_TAG = "fragment_photo_gallery";
    public static final String FRAGMENT_VIDEO_GALLERY_TAG = "fragment_video_gallery";
    public static final String FRAGMENT_DOCUMENT_GALLERY_TAG = "fragment_document_gallery";
    public static final String FRAGMENT_MEMBER_GALLERY_TAG = "fragment_member_gallery";
    public static final String FRAGMENT_SEARCH_MEMBER_GALLERY_TAG = "fragment_search_member_gallery";
    public static final String FRAGMENT_PREVIEW_MEMBER_GALLERY_TAG = "fragment_preview_member_gallery";
    public static final String FRAGMENT_LEGAL = "fragment_legal";
    public static final String FRAGMENT_LEGAL_DESCRIPTION = "fragment_legal_description";
    public static final String NO_FRAGMENT_TAG = "no_fragment_tag";
    //endregion FRAGMENT TAGS//

    //region WEB SERVICE ID //
    public static final int WS_KEY_PUBLIC_TEST = 0;
    public static final int WS_KEY_LOGIN_SERVICE = 1;
    public static final int WS_KEY_TASK_SERVICE_NEWS = 2;
    public static final int WS_KEY_TASK_SERVICE_PROGRESS = 3;
    public static final int WS_KEY_TASK_SERVICE_PENDING = 4;
    public static final int WS_KEY_UPDATE_TASK = 5;
    public static final int WS_KEY_UPDATE_TASK_WITH_PICTURE = 6;
    public static final int WS_KEY_UPDATE_TASK_FILE = 8;
    public static final int WS_KEY_SEND_LOCATION = 9;
    public static final int WS_KEY_SEND_LOCATION_HIDDEN = 10;
    @Deprecated
    public static final int WS_KEY_FIND_TASK = 11;
    @Deprecated
    public static final int WS_KEY_TASK_SERVICE_REVISION = 12; //ONLY WEB
    public static final int WS_KEY_TASK_SERVICE_CLOSE = 13;
    public static final int WS_KEY_ALL_TASKS = 14;
    public static final int WS_KEY_ALL_USERS = 15;
    public static final int WS_KEY_SERVER_SYNC = 16;
    public static final int WS_KEY_UPDATE_VIDEO = 17;
    public static final int WS_KEY_FORGET_USERNAME_SERVICE = 18;
    public static final int WS_KEY_CONNECTION = 19;
    public static final int WS_KEY_CHECK_VERSION = 20;
    public static final int WS_KEY_ITEM_PHOTO_GALLERY = 21;
    public static final int WS_KEY_ITEM_DELETE = 22;
    public static final int WS_KEY_ITEM_SAVE_DESCRIPTION = 23;
    public static final int WS_KEY_ITEM_ADD_PHOTO = 24;
    public static final int WS_KEY_ITEM_SYNC = 25;
    public static final int WS_KEY_ITEM_SYNC_HOME = 26;
    public static final int WS_KEY_ITEM_VIDEO_GALLERY = 27;
    public static final int WS_KEY_ITEM_ADD_VIDEO = 28;
    public static final int WS_KEY_ITEM_DOCUMENT_GALLERY = 29;
    public static final int WS_KEY_ITEM_ADD_DOCUMENT = 30;
    public static final int WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT = 31;
    public static final int WS_KEY_ITEM_MEMBER_GALLERY = 32;
    public static final int WS_KEY_ITEM_SEARCH_GALLERY = 33;
    public static final int WS_KEY_ITEM_PREVIEW_GALLERY = 34;
    public static final int WS_KEY_ITEM_ADD_PREVIEW_MEMBER = 35;
    public static final int WS_KEY_ITEM_DELETE_PREVIEW_MEMBER = 36;
    public static final int WS_KEY_ITEM_ADD_MEMBER = 37;
    public static final int WS_KEY_ITEM_SAVE_MEMBER = 38;
    //endregion WEB SERVICE ID//

    //region SOAP CONFIGURATION//
    /**
     * NEVER CHANGE IP SERVICE_NAMESPACE
     * <p>
     * TEXIUM MANAGER
     * *********************************
     * <p>
     * WEB_SERVICE_URL = "http://192.168.1.138/SistemaMedioAmbienteDF/ServicioWeb/Servicio_Android.asmx";
     * <p>
     * **********************************
     * SEDEMA MANAGER
     * <p>
     * WEB_SERVICE_URL http://192.168.1.98/ServicioWeb/Servicio_Android.asmx
     * <p>
     * **********************************
     */
    public static final String WEB_SERVICE_SOAP_ACTION = "192.168.12.10/LoginMovil";
    public static final String WEB_SERVICE_SOAP_ACTION_ALL_TASKS = "192.168.12.10/ListadoTareas";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_TASK = "192.168.12.10/ActualizarTarea";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_LOCATION = "192.168.12.10/SincronizarCoordenadas";
    public static final String WEB_SERVICE_SOAP_ACTION_SEND_FILE = "192.168.12.10/AgregarArchivos";
    public static final String WEB_SERVICE_SOAP_ACTION_ALL_USERS = "192.168.12.10/ListadoUsuarios";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_VIDEO = "192.168.12.10/AgregarVideo";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_DOCUMENT = "192.168.12.10/AgregarPDF";
    public static final String WEB_SERVICE_SOAP_ACTION_FORGET_USERNAME = "192.168.12.10/RecuperarContrasena";
    public static final String WEB_SERVICE_SOAP_ACTION_DELETE_PHOTO_FILE = "192.168.12.10/EliminarArchivo";
    public static final String WEB_SERVICE_SOAP_ACTION_SAVE_PHOTO_FILE = "192.168.12.10/AgregarArchivo";
    public static final String WEB_SERVICE_SOAP_ACTION_UPDATE_PHOTO_FILE = "192.168.12.10/ModificarArchivo";
    public static final String WEB_SERVICE_SOAP_ACTION_APP_VERSION = "192.168.12.10/ObtenerVersionMovil";
    public static final String WEB_SERVICE_SOAP_ACTION_TASK_FILES = "192.168.12.10/ArchivosTareas";
    public static final String WEB_SERVICE_SOAP_ACTION_ADD_MEMBER = "192.168.12.10/AgregarMiembro";
    public static final String WEB_SERVICE_SOAP_ACTION_DELETE_MEMBER = "192.168.12.10/EliminarMiembro";
    public static final String WEB_SERVICE_SOAP_ACTION_ALL_TASKS_MEMBERS = "192.168.12.10/ConsultarMiembrosTarea";
    public static final String WEB_SERVICE_SOAP_ACTION_SERVER_TASK = "192.168.12.10/ConsultarTarea";


    public static final String WEB_SERVICE_NAMESPACE = "192.168.12.10";
    public static final String WEB_SERVICE_PARTIAL_URL = "http://170.0.12.51"; //Sin / al final

    //Producción//
    public static final String WEB_SERVICE_COMPLETE_URL = "http://170.0.12.51/ServicioWeb/Servicio_Android.asmx"; //Ruta completa publicada
    public static final String WEB_SERVICE_URL = "/ServicioWeb/Servicio_Android.asmx"; //Ruta de las carpetas

    //endregion SOAP CONFIGURATION//

    //region SOAP PROPERTIES//
    public static final String SOAP_PROPERTY_DIFFGRAM = "diffgram";
    public static final String SOAP_PROPERTY_NEW_DATA_SET = "NewDataSet";
    //endregion SOAP PROPERTIES//

    //region SOAP OPERATION//
    public static final String WEB_SERVICE_METHOD_NAME_LOGIN = "LoginMovil";
    public static final String WEB_SERVICE_METHOD_NAME_ALL_TASKS = "ListadoTareas";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_TASK = "ActualizarTarea";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_LOCATION = "SincronizarCoordenadas";
    public static final String WEB_SERVICE_METHOD_NAME_SEND_FILE = "AgregarArchivos";
    public static final String WEB_SERVICE_METHOD_NAME_ALL_USERS = "ListadoUsuarios";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_VIDEO = "AgregarVideo";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_DOCUMENT = "AgregarPDF";
    public static final String WEB_SERVICE_METHOD_NAME_FORGET_USERNAME = "RecuperarContrasena";
    public static final String WEB_SERVICE_METHOD_NAME_DELETE_PHOTO_FILE = "EliminarArchivo";
    public static final String WEB_SERVICE_METHOD_NAME_SAVE_PHOTO_FILE = "AgregarArchivo";
    public static final String WEB_SERVICE_METHOD_NAME_UPDATE_PHOTO_FILE = "ModificarArchivo";
    public static final String WEB_SERVICE_METHOD_NAME_APP_VERSION = "ObtenerVersionMovil";
    public static final String WEB_SERVICE_METHOD_NAME_TASK_FILES = "ArchivosTareas";
    public static final String WEB_SERVICE_METHOD_NAME_ADD_MEMBER = "AgregarMiembro";
    public static final String WEB_SERVICE_METHOD_NAME_DELETE_MEMBER = "EliminarMiembro";
    public static final String WEB_SERVICE_METHOD_NAME_ALL_TASKS_MEMBERS = "ConsultarMiembrosTarea";
    public static final String WEB_SERVICE_METHOD_NAME_SERVER_TASK = "ConsultarTarea";


    //endregion SOAP OPERATION//

    //region WEB SERVICE PARAMS//
    public static final String WEB_SERVICE_PARAM_LOGIN_USERNAME = "NombreUsuario";
    public static final String WEB_SERVICE_PARAM_LOGIN_PASSWORD = "ContrasenaUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_ID_TEAM = "IDCuadrilla";
    public static final String WEB_SERVICE_PARAM_TASK_STATUS = "IDEstatus";
    public static final String WEB_SERVICE_PARAM_TASK_ID = "IDTarea";
    public static final String WEB_SERVICE_PARAM_ID = "ID";
    public static final String WEB_SERVICE_PARAM_SYSTEM_ID = "IDSistema";
    public static final String WEB_SERVICE_PARAM_SYSTEM_EXTENSION = "IDTipo";
    public static final String WEB_SERVICE_PARAM_TASK_COMMENT = "Observacion";
    public static final String WEB_SERVICE_PARAM_TASK_CAUSE = "CausasImposibilidad";
    public static final String WEB_SERVICE_PARAM_TASK_FILE_NUMBER = "Expediente";
    public static final String WEB_SERVICE_PARAM_TASK_CLOSURE = "Clausurado";
    public static final String WEB_SERVICE_PARAM_TASK_ID_USER = "IDUsuario";
    public static final String WEB_SERVICE_PARAM_TASK_LATITUDE = "Latitud";
    public static final String WEB_SERVICE_PARAM_TASK_LONGITUDE = "Longitud";
    public static final String WEB_SERVICE_PARAM_TASK_CONNECTION = "Conectado";
    public static final String WEB_SERVICE_PARAM_TASK_FILE = "Archivo";
    public static final String WEB_SERVICE_PARAM_CODE_FILE = "Codigo";
    public static final String WEB_SERVICE_PARAM_OBJECT_STRING = "string";
    public static final String WEB_SERVICE_PARAM_VIDEO_PART_NUMBER = "NumeroParte";
    public static final String WEB_SERVICE_PARAM_VIDEO_LAST_ONE = "Ultimo";
    public static final String WEB_SERVICE_PARAM_LOGIN_EMAIL = "CorreoElectronico";
    public static final String WEB_SERVICE_PARAM_DESCRIPTION = "Descripcion";
    public static final String WEB_SERVICE_PARAM_NAME = "Nombre";
    public static final String WEB_SERVICE_PARAM_LOCATION_DATE = "Fecha";
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
    public static final String SOAP_OBJECT_KEY_LOGIN_ID_TEAM_ACTUAL = "IDCuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAM_NAME = "nombrecuadrilla";
    public static final String SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION = "coordenadas";
    public static final String SOAP_OBJECT_KEY_LOGIN_LATITUDE = "Latitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LONGITUDE = "Longitud";
    public static final String SOAP_OBJECT_KEY_LOGIN_LAST_CONNECTION = "ultimaconexion";
    public static final String SOAP_OBJECT_KEY_LOGIN_PASSWORD = "contrasenausuario";
    public static final String SOAP_OBJECT_KEY_PROFILE_PICTURE = "imagen";
    public static final String SOAP_OBJECT_KEY_ID = "ID";
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
    public static final String SOAP_OBJECT_KEY_TASK_URL = "Ruta";
    public static final String SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS = "RutaServidor";
    public static final String SOAP_OBJECT_KEY_TASK_SYSTEM_ID = "IDSistema";
    public static final String SOAP_OBJECT_KEY_TASK_PHOTO_FILE_ID = "IDArchivo";
    public static final String SOAP_OBJECT_KEY_TASK_ID = "IDTarea";
    public static final String SOAP_OBJECT_KEY_DECODE_BASE_64 = "Base64";
    public static final String SOAP_OBJECT_KEY_FILE_DESCRIPTION = "Descripcion";
    public static final String SOAP_OBJECT_KEY_TASK_ACTUAL_USER_ID = "IDUsuarioActual";
    //endregion SOAP KEYS//

    //region ACTIVITY PARAMS//
    public static final String ACTIVITY_EXTRA_PARAMS_LOGIN = "data";
    public static final String ACTIVITY_EXTRA_PARAMS_TASK_GALLERY = "task_gallery_info";
    public static final String ACTIVITY_EXTRA_PARAMS_TASK_GALLERY_DESCRIPTION = "task_gallery_description";
    public static final String ACTIVITY_EXTRA_PARAMS_ACTUAL_FRAGMENT = "actual_fragment";
    public static final String ACTIVITY_EXTRA_PARAMS_ACTUAL_TASK_NOTIFICATION = "actual_task_notification";
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

    public static Map<Integer, String> MAP_STATUS_NAME;

    static {
        MAP_STATUS_NAME = new HashMap<>();
        MAP_STATUS_NAME.put(TASK_PRIORITY_LOW_ID, TASK_PRIORITY_LOW_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_MID_ID, TASK_PRIORITY_MID_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_TOP_ID, TASK_PRIORITY_TOP_STR);
        MAP_STATUS_NAME.put(TASK_PRIORITY_EXPRESS_ID, TASK_PRIORITY_EXPRESS_STR);
    }

    /*
    public static Map<Integer,Float> MAP_STATUS_COLOR;
    static {
        MAP_STATUS_COLOR = new HashMap<>();
        MAP_STATUS_COLOR.put(TASK_PRIORITY_LOW_ID,BitmapDescriptorFactory.HUE_CYAN);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_MID_ID,BitmapDescriptorFactory.HUE_YELLOW);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_TOP_ID,BitmapDescriptorFactory.HUE_ORANGE);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_EXPRESS_ID,BitmapDescriptorFactory.HUE_RED);
    }
    */

    public static Map<Integer, Integer> MAP_STATUS_ICON;

    static {
        MAP_STATUS_ICON = new HashMap<>();
        MAP_STATUS_ICON.put(TASK_PRIORITY_LOW_ID, R.drawable.task_priority_low);
        MAP_STATUS_ICON.put(TASK_PRIORITY_MID_ID, R.drawable.task_priority_mid);
        MAP_STATUS_ICON.put(TASK_PRIORITY_TOP_ID, R.drawable.task_priority_top);
        MAP_STATUS_ICON.put(TASK_PRIORITY_EXPRESS_ID, R.drawable.task_priority_express);
    }

    public static Map<Integer, Integer> MAP_STATUS_COLOR;

    static {
        MAP_STATUS_COLOR = new HashMap<>();
        MAP_STATUS_COLOR.put(TASK_PRIORITY_LOW_ID, R.color.colorPriorityLow);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_MID_ID, R.color.colorPriorityMid);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_TOP_ID, R.color.colorPriorityTop);
        MAP_STATUS_COLOR.put(TASK_PRIORITY_EXPRESS_ID, R.color.colorPriorityExpress);
    }

    public static Map<Integer, String> MAP_STATUS_FRAGMENT;

    static {
        MAP_STATUS_FRAGMENT = new HashMap<>();
        MAP_STATUS_FRAGMENT.put(NEWS_TASK, FRAGMENT_NEWS_TAG);
        MAP_STATUS_FRAGMENT.put(PROGRESS_TASK, FRAGMENT_PROGRESS_TAG);
        MAP_STATUS_FRAGMENT.put(PENDING_TASK, FRAGMENT_PENDING_TAG);
        MAP_STATUS_FRAGMENT.put(CLOSE_TASK, FRAGMENT_CLOSE_TAG);
        MAP_STATUS_FRAGMENT.put(REVISION_TASK, NO_FRAGMENT_TAG);
        MAP_STATUS_FRAGMENT.put(CANCEL_TASK, NO_FRAGMENT_TAG);
    }


    //endregion ACCESS TO MAP PRIORITY//

    //region SYNC BUTTON//
    public static final int ITEM_SYNC_SERVER_DEFAULT = 1;
    public static final int ITEM_SYNC_LOCAL_TABLET = 2;
    public static final int ITEM_SYNC_SERVER_CLOUD = 3;
    public static final int ITEM_SYNC_SERVER_CLOUD_OFF = 4;
    public static final int ITEM_SYNC_SERVER_DELETE = 5;

    //endregion//
}
