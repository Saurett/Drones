package texium.mx.drones.services;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;

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
        }

        return soapObject;
    }

    public static SoapObject getServerTaskList(Integer idTeam, Integer idStatus) {
        SoapObject soapObject = new SoapObject();
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_TASKLIST;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_TASK;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = Constants.WEB_SERVICE_URL;

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_TEAM, idTeam);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_STATUS, idStatus);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapObject;
    }

    public static SoapObject checkUser(String username, String password) {
        SoapObject soapObject = new SoapObject();
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_LOGIN;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = Constants.WEB_SERVICE_URL;

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_USERNAME, username);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_PASSWORD, password);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapObject;
    }

    public static SoapPrimitive updateTask(Integer task, String comment,Integer status,Integer user) {
        SoapPrimitive soapPrimitive = null;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_TASK;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_TASK;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = Constants.WEB_SERVICE_URL;

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID, task);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_COMMENT, comment);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_STATUS, status);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapPrimitive;
    }

    public static SoapPrimitive updateLocation(Integer team, String latitude, String longitude,Integer user) {
        SoapPrimitive soapPrimitive = null;
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION_UPDATE_LOCATION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_UPDATE_LOCATION;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = Constants.WEB_SERVICE_URL;

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_TEAM, team);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_LATITUDE, latitude);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_LONGITUDE, longitude);
            Request.addProperty(Constants.WEB_SERVICE_PARAM_TASK_ID_USER, user);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapPrimitive;
    }
}
