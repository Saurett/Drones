package texium.mx.drones;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.net.ConnectException;

import texium.mx.drones.databases.BDTasksManager;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameLogin, passwordLogin, linkLogin;
    private View mLoginFormView,mProgressView;
    private Button loginButton, cleanButton, forgetUsername, connectivity;

    private int actionFlag = Constants.LOGIN_FORM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_civar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        loginButton = (Button) findViewById(R.id.login_button);
        cleanButton = (Button) findViewById(R.id.clean_button);
        forgetUsername = (Button) findViewById(R.id.forget_my_username);
        connectivity = (Button) findViewById(R.id.connectivity);

        usernameLogin = (EditText) findViewById(R.id.username_login);
        passwordLogin = (EditText) findViewById(R.id.password_login);
        linkLogin = (EditText) findViewById(R.id.link);

        linkLogin.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        forgetUsername.setOnClickListener(this);
        connectivity.setOnClickListener(this);

        AsyncCallWS wsAllTask = new AsyncCallWS(Constants.WS_KEY_ALL_USERS);
        wsAllTask.execute();
    }

    //Login action
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void attemptLogin() {

        boolean cancel = false;

        usernameLogin.setError(null);
        passwordLogin.setError(null);

        String username = usernameLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        String link = linkLogin.getText().toString();

        if (TextUtils.isEmpty(password) && ((actionFlag == Constants.LOGIN_FORM)
            || (actionFlag == Constants.CONNECTIVITY_FORM))) {
            passwordLogin.setError(getString(R.string.password_login_error),null);
            passwordLogin.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameLogin.setError(getString(R.string.username_login_error),null);
            usernameLogin.requestFocus();
            cancel = true;
        }


        if ((TextUtils.isEmpty(link)) && (actionFlag == Constants.CONNECTIVITY_FORM)) {
            linkLogin.setError("Ingrese cadena de conecxión",null);
            linkLogin.requestFocus();
            cancel = true;
        }

        if (!cancel) {

            switch (actionFlag) {
                case Constants.LOGIN_FORM:
                    AsyncCallWS wsLogin = new AsyncCallWS(Constants.WS_KEY_LOGIN_SERVICE,username,password);
                    wsLogin.execute();
                    break;
                case Constants.FORGET_USERNAME_FORM:
                    AsyncCallWS wsForget = new AsyncCallWS(Constants.WS_KEY_FORGET_USERNAME_SERVICE,username,null);
                    wsForget.execute();
                    break;
                case Constants.CONNECTIVITY_FORM:
                    AsyncCallWS wsConnection = new AsyncCallWS(Constants.WS_KEY_CONNECTION,username,password,link.trim());
                    wsConnection.execute();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Hola mundo", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    //Clean all login values
    private void cleanAllLogin() {

        actionFlag = Constants.LOGIN_FORM;

        usernameLogin.setError(null);
        passwordLogin.setError(null);
        linkLogin.setError(null);

        usernameLogin.setText(null);
        passwordLogin.setText(null);
        linkLogin.setText(null);

        usernameLogin.setHint(R.string.default_enter_username);

        cleanButton.setText("BORRAR");
        loginButton.setText("ENTRAR");

        passwordLogin.setVisibility(View.VISIBLE);
        linkLogin.setVisibility(View.INVISIBLE);
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
            case R.id.forget_my_username:
                actionFlag = Constants.FORGET_USERNAME_FORM;

                usernameLogin.setHint(R.string.default_entrer_email);

                usernameLogin.setText(null);
                passwordLogin.setText(null);

                cleanButton.setText("CANCELAR");
                loginButton.setText("ENVIAR");

                passwordLogin.setVisibility(View.INVISIBLE);
                linkLogin.setVisibility(View.INVISIBLE);
                break;
            case R.id.connectivity:

                actionFlag = Constants.CONNECTIVITY_FORM;

                usernameLogin.setHint(R.string.default_enter_username);

                usernameLogin.setText(null);
                passwordLogin.setText(null);

                try {
                    linkLogin.setText(BDTasksManagerQuery.getPartialServer(getApplicationContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cleanButton.setText("CANCELAR");
                loginButton.setText("ENTRAR");

                linkLogin.setVisibility(View.VISIBLE);
                passwordLogin.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean>  {

        private SoapObject soapObject;
        private SoapPrimitive soapPrimitive;
        private Integer webServiceOperation;
        private String username;
        private String password;
        private String webServiceLink;
        private String textError;
        private Boolean localAccess;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
        }

        private AsyncCallWS(Integer wsOperation,String wsUsername, String wsPassword) {
            webServiceOperation = wsOperation;
            username = wsUsername;
            password = wsPassword;
            textError = "";
            localAccess = false;
        }

        private AsyncCallWS(Integer wsOperation,String wsUsername, String wsPassword, String wsTempLink) {
            webServiceOperation = wsOperation;
            username = wsUsername;
            password = wsPassword;
            webServiceLink = wsTempLink;
            textError = "";
            localAccess = false;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation;

            try{
                switch (webServiceOperation) {
                    case Constants.WS_KEY_PUBLIC_TEST:
                        SoapServices.calculate(username);
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_LOGIN_SERVICE:
                        soapObject = SoapServices.checkUser(getApplicationContext(),username,password);
                        Integer id = Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString());

                        validOperation = (id > 0);
                        break;
                    case Constants.WS_KEY_ALL_USERS:
                        soapObject = SoapServices.getServerAllUsers(getApplicationContext());
                        validOperation = (soapObject.getPropertyCount() > 0);
                        break;
                    case Constants.WS_KEY_FORGET_USERNAME_SERVICE:
                        soapPrimitive = SoapServices.forgetUsername(getApplicationContext(),username);
                        validOperation = (soapPrimitive != null);
                        break;
                    case Constants.WS_KEY_CONNECTION:
                        validOperation = false;
                        String tempPartialServer = BDTasksManagerQuery.getPartialServer(getApplicationContext());

                        if (tempPartialServer.equals(webServiceLink)) {
                            textError = "La cadena de conexión no ha sido modificada";
                            return validOperation;
                        }

                        Users u = new Users();

                        u.setUserName("admin");
                        u.setPassword("Sedema#2016");
                        u.setIdActor(-1);

                        if (!(u.getUserName().equals(username.trim()))
                                && !(u.getPassword().equals(password.trim()))) {
                            u.setUserName(username.trim());
                            u.setPassword(password.trim());

                            Users tempUser = BDTasksManagerQuery.getUserByCredentials(getApplicationContext(), u);
                            validOperation = (tempUser.getIdUser() != null);
                            localAccess = (tempUser.getIdUser() != null);
                            textError = (tempUser.getIdUser() != null) ? "" : getString(R.string.default_user_unregister);

                            Log.i("INFO MSG", textError);

                            if (!validOperation) return validOperation;
                            u.setIdUser(tempUser.getIdUser());
                        }

                        //Create a new string
                        Integer cveLink = BDTasksManagerQuery.getCveLink(getApplicationContext());
                        validOperation = true;

                        if (null == cveLink) {
                            BDTasksManagerQuery.addLink(getApplicationContext(),webServiceLink,u);
                        } else {
                            BDTasksManagerQuery.updateLink(getApplicationContext(),cveLink,u);
                            BDTasksManagerQuery.addLink(getApplicationContext(),webServiceLink,u);
                        }

                        break;
                    default:
                        validOperation = false;
                        break;
                }
            } catch (ConnectException e){

                textError = (e != null) ? e.getMessage() : "Unknown error";
                validOperation = false;
                if (e != null) e.printStackTrace();

                Log.e("LoginUserException: ", "Unknown error : " + textError);

                if (webServiceOperation == Constants.WS_KEY_LOGIN_SERVICE) {
                    try {

                        Users u = new Users();
                        u.setUserName(username.trim());
                        u.setPassword(password.trim());

                        Users tempUser = BDTasksManagerQuery.getUserByCredentials(getApplicationContext(), u);
                        validOperation = (tempUser.getIdUser() != null);
                        localAccess = (tempUser.getIdUser() != null);
                        textError = (tempUser.getIdUser() != null) ? "" : getString(R.string.default_user_unregister);

                        Log.i("INFO MSG", textError);

                        if (!validOperation) return validOperation;

                        //Check validOperation with the user password
                        validOperation = (password.equals(tempUser.getPassword()));
                        textError = (validOperation) ? textError : getString(R.string.default_incorrect_password);

                        Log.i("INFO MSG", textError);

                    } catch (Exception ex) {
                        textError = ex.getMessage();

                        ex.printStackTrace();
                        Log.e("ValidationUserException: ", "Unknown error");
                    }
                }

            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if(success) {

                Intent intentNavigationDrawer = new Intent(MainActivity.this,NavigationDrawerActivity.class);
                Users user = new Users();

                switch (webServiceOperation) {
                    case Constants.WS_KEY_LOGIN_SERVICE:

                        if (localAccess) {

                            try {

                                user.setUserName(username.trim());
                                user.setPassword(password.trim());

                                Users tempUser = BDTasksManagerQuery.getUserByCredentials(getApplicationContext(), user);

                                if (tempUser.getIdUser() != 0) {

                                    if(Integer.valueOf(tempUser.getIdActor()) == 0) {
                                        Toast.makeText(MainActivity.this,getString(R.string.no_user_team_login_error), Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    user = tempUser;
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("SQLite Exception ", ex.getMessage());
                            }

                        } else {

                            if(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_TEAM).toString()) == 0) {
                                Toast.makeText(MainActivity.this,getString(R.string.no_user_team_login_error), Toast.LENGTH_LONG).show();
                                return;
                            }

                            SoapObject location = (SoapObject) soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION);

                            user.setIdUser(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString()));
                            user.setUserName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_USERNAME).toString());
                            user.setIdActor(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_ACTOR).toString()));
                            user.setActorName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_NAME).toString());
                            user.setActorType(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPE).toString()));
                            user.setActorTypeName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPENAME).toString());
                            user.setIdTeam(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_TEAM).toString()));
                            user.setTeamName(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_NAME).toString());
                            user.setLatitude(Double.valueOf(location.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LATITUDE).toString()));
                            user.setLongitude(Double.valueOf(location.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LONGITUDE).toString()));
                            user.setLastTeamConnection(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LAST_CONNECTION).toString());
                        }


                        usernameLogin.clearFocus();
                        passwordLogin.clearFocus();

                        intentNavigationDrawer.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, user);
                        cleanAllLogin();
                        startActivity(intentNavigationDrawer);
                        break;
                    case Constants.WS_KEY_ALL_USERS:

                        for (int i = 0; i < soapObject.getPropertyCount(); i ++) {

                            SoapObject soTemp = (SoapObject) soapObject.getProperty(i);
                            SoapObject uLocation = (SoapObject) soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION);

                            user.setIdUser(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString()));
                            user.setUserName(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_USERNAME).toString());
                            user.setIdActor(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_ACTOR).toString()));
                            user.setActorName(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_NAME).toString());
                            user.setActorType(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPE).toString()));
                            user.setActorTypeName(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ACTOR_TYPENAME).toString());
                            user.setIdTeam(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_TEAM).toString()));
                            user.setTeamName(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_NAME).toString());
                            user.setLatitude(Double.valueOf(uLocation.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LATITUDE).toString()));
                            user.setLongitude(Double.valueOf(uLocation.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LONGITUDE).toString()));
                            user.setLastTeamConnection(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_LAST_CONNECTION).toString());
                            user.setPassword(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_PASSWORD).toString());

                            try {
                                Users tempUser = BDTasksManagerQuery.getUserById(getApplicationContext(), user);

                                if (tempUser.getIdUser() == null) BDTasksManagerQuery.addUser(getApplicationContext(), user);
                                if (tempUser.getIdUser() != null) BDTasksManagerQuery.updateUser(getApplicationContext(), user);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("SQLite Exception ", ex.getMessage());
                            }
                        }

                        break;
                    case Constants.WS_KEY_FORGET_USERNAME_SERVICE:
                        Toast.makeText(MainActivity.this,soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_CONNECTION:
                        Toast.makeText(MainActivity.this, "Cadena de conexión actualizada correctamente", Toast.LENGTH_LONG).show();
                        cleanAllLogin();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "TEST", Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                String tempTextMsg = (textError.isEmpty() ? getString(R.string.default_login_error) : textError);
                Toast.makeText(MainActivity.this,tempTextMsg, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            super.onCancelled();
        }
    }
}
