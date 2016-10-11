package texium.mx.drones.services;

import android.content.Context;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 01/03/2016.
 */
public class SoapServices {

    public static SoapObject calculate(String text) {

        SoapObject soapObject = new SoapObject();

        String SOAP_ACTION = "http://www.w3schools.com/xml/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/xml/";
        String URL = "http://www.w3schools.com/xml/tempconvert.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("Celsius", text);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Soap Exception", ex.getMessage());
        }

        return soapObject;
    }

    public static SoapObject getServerAllTasks(Context context, Integer idUser, Integer idStatus) throws Exception {
        SoapObject soapObject;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_ALL_TASKS;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_ALL_TASKS;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, idUser);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_STATUS, idStatus);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapObject;
    }

    public static SoapObject checkUser(Context context,String username, String password) throws Exception {
        SoapObject soapObject;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_LOGIN;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_USERNAME, username);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_PASSWORD, password);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapObject;
    }

    public static SoapPrimitive forgetUsername(Context context,String email) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_FORGET_USERNAME;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_FORGET_USERNAME;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_EMAIL, email);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapObject getServerAllUsers(Context context) throws Exception {
        SoapObject soapObject;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_ALL_USERS;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_ALL_USERS;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {
            if (e != null) {
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapObject;
    }

    public static SoapPrimitive updateTask(Context context,Integer task, String comment,Integer status,Integer user,List<String> encodedFile) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_TASK;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_TASK;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, task);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_COMMENT, comment);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_STATUS, status);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);

            SoapObject soapFiles = new SoapObject(NAMESPACE, Constants.WEB_SERVICE_PARAM_TASK_FILE);

            for (String encode: encodedFile) {
                soapFiles.addProperty(Constants.WEB_SERVICE_PARAM_OBJECT_STRING,encode);
            }

            Request.addSoapObject(soapFiles);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive updateVideoFiles(Context context,Integer task,Integer user, String encodeFile, Integer partNumber, Boolean lastOne, String description ) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_VIDEO;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_VIDEO;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, task);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_DESCRIPTION, description);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_CODE_FILE, encodeFile);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_VIDEO_PART_NUMBER, partNumber);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_VIDEO_LAST_ONE, lastOne);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);


            HttpTransportSE transport = new HttpTransportSE(URL,2000000);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive updateDocumentFiles(Context context,Integer task,Integer user, String encodeFile, Integer partNumber, Boolean lastOne, String description ) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_DOCUMENT;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_DOCUMENT;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, task);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_DESCRIPTION, description);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_CODE_FILE, encodeFile);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_VIDEO_PART_NUMBER, partNumber);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_VIDEO_LAST_ONE, lastOne);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);


            HttpTransportSE transport = new HttpTransportSE(URL,2000000);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive sendFile(Context context,Integer task, Integer user, List<String> encodedImage) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_SEND_FILE;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_SEND_FILE;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, task);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);
            SoapObject soapFiles = new SoapObject(NAMESPACE, Constants.WEB_SERVICE_PARAM_TASK_FILE);

            for (String encode: encodedImage) {
                soapFiles.addProperty(Constants.WEB_SERVICE_PARAM_OBJECT_STRING,encode);
            }

            Request.addSoapObject(soapFiles);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive updateLocation(Context context, String latitude, String longitude,Integer user, Boolean connection) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_LOCATION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_LOCATION;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_LATITUDE, latitude);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_LONGITUDE, longitude);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_CONNECTION, connection);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapObject checkAppVersion(Context context) throws Exception {
        SoapObject soapObject;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_APP_VERSION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_APP_VERSION;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapObject;
    }

    public static SoapObject getTaskFiles(Context context, Integer idTask, Integer idSystem, Integer extension) throws Exception {
        SoapObject soapObject;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_TASK_FILES;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_TASK_FILES;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, idTask);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_SYSTEM_ID, idSystem);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_SYSTEM_EXTENSION, extension);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapObject;
    }

    public static SoapPrimitive deletePhotoFile(Context context,Integer idPhotoFile, Integer idUser) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_DELETE_PHOTO_FILE;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_DELETE_PHOTO_FILE;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_PHOTO_FILE_ID, idPhotoFile);
            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID, idUser);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive savePhotoFile(Context context, TaskGallery taskGallery, Integer idTask, Integer idUser) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_SAVE_PHOTO_FILE;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_SAVE_PHOTO_FILE;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_ID, idTask);
            Request.addProperty(Constants.SOAP_OBJECT_KEY_DECODE_BASE_64, taskGallery.getBase_package());
            Request.addProperty(Constants.SOAP_OBJECT_KEY_FILE_DESCRIPTION, taskGallery.getDescription());
            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID, idUser);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }

    public static SoapPrimitive updatePhotoFile(Context context, TaskGallery taskGallery, Integer idUser) throws Exception {
        SoapPrimitive soapPrimitive;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_PHOTO_FILE;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_PHOTO_FILE;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = BDTasksManagerQuery.getServer(context);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_PHOTO_FILE_ID, taskGallery.getId());
            Request.addProperty(Constants.SOAP_OBJECT_KEY_FILE_DESCRIPTION, taskGallery.getDescription());
            Request.addProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID, idUser);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (EOFException e ) {
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (ConnectException e) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SocketTimeoutException e ) {
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (HttpResponseException e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (SoapFault e){
            e.printStackTrace();
            throw  new ConnectException(context.getString(R.string.default_connect_error));
        } catch (Exception e) {

            if (e != null) {
                e.printStackTrace();
                throw new Exception(context.getString(R.string.default_exception_error));
            } else {
                throw  new ConnectException(context.getString(R.string.default_connect_error));
            }
        }

        return soapPrimitive;
    }
}
