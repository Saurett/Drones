package texium.mx.drones;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Response";
    SoapObject soapObject;

    EditText usernameLogin;
    EditText passwordLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button cleanButton = (Button) findViewById(R.id.clean_button);

        usernameLogin = (EditText) findViewById(R.id.username_login);
        passwordLogin = (EditText) findViewById(R.id.password_login);

        loginButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);

    }

    //Login action
    private void attemptLogin() {


        boolean cancel = false;

        usernameLogin.setError(null);
        passwordLogin.setError(null);

        String username = usernameLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if (TextUtils.isEmpty(password)) {
            passwordLogin.setError(getString(R.string.password_login_error));
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameLogin.setError(getString(R.string.username_login_error));
            cancel = true;
        }

        if (!cancel) {

            AsyncCallWS task = new AsyncCallWS(Constants.WS_KEY_LOGIN_SERVICE);
            task.execute();
        }

    }

    //Clean all login values
    private void cleanAllLogin() {

        usernameLogin.setText(null);
        passwordLogin.setText(null);
    }


    //All OnClick login actions
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                attemptLogin();
                break;
            case R.id.clean_button:
                cleanAllLogin();
                break;
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean>  {

        private Integer webServiceOperation;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            switch (webServiceOperation) {
                case Constants.WS_KEY_PUBLIC_TEST:
                    calculate();
                    validOperation = true;
                    break;
                case Constants.WS_KEY_LOGIN_SERVICE:
                    SoapObject soLogin = checkUser();
                    Integer id = Integer.valueOf(soLogin.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString());

                    if(id > 0)  validOperation = true;

                    break;
                default:
                    Toast.makeText(MainActivity.this, getString(R.string.default_ws_operation), Toast.LENGTH_LONG).show();
                    validOperation = false;
                    break;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(success) {
                //Oficial Login To Navigation Drawer//
                Intent intentNavigationDrawer = new Intent(MainActivity.this,NavigationDrawerActivity.class);

                SoapObject location = (SoapObject) soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION);

                Users user = new Users();

                user.setIdUser(Long.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString()));
                user.setUserName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_USERNAME).toString());
                user.setIdActor(Long.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_ACTOR).toString()));
                user.setActorName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTORNAME).toString());
                user.setActorType(Long.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPE).toString()));
                user.setActorTypeName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPENAME).toString());
                user.setIdTeam(Long.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_TEAM).toString()));
                user.setTeamName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAMNAME).toString());
                user.setLatitude(Double.valueOf(location.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LATITUDE).toString()));
                user.setLongitude(Double.valueOf(location.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LONGITUDE).toString()));
                user.setLastTeamConexion(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LAST_CONEXION).toString());

                intentNavigationDrawer.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, user);
                cleanAllLogin();
                startActivity(intentNavigationDrawer);
            } else {
                Toast.makeText(MainActivity.this,getString(R.string.default_login_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    public SoapObject checkUser() {
        try {
            String SOAP_ACTION = Constants.WEB_SERVICE_SOAP_ACTION;
            String METHOD_NAME = Constants.WEB_SERVICE_METHOD_NAME_LOGIN;
            String NAMESPACE = Constants.WEB_SERVICE_NAMESPACE;
            String URL = Constants.WEB_SERVICE_URL;

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_USERNAME,usernameLogin.getText().toString());
            Request.addProperty(Constants.WEB_SERVICE_PARAM_LOGIN_PASSWORD, passwordLogin.getText().toString());

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

    /**
     * Test Public WebService in W3Schools
     */
    public void calculate() {
        String SOAP_ACTION = "http://www.w3schools.com/xml/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/xml/";
        String URL = "http://www.w3schools.com/xml/tempconvert.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("Celsius", usernameLogin.getText().toString());

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            soapObject = (SoapObject) soapEnvelope.getResponse();

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
