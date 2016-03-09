package texium.mx.drones;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoapObject soapObject;
    private EditText usernameLogin, passwordLogin;
    private View mLoginFormView,mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

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

            AsyncCallWS wsLogin = new AsyncCallWS(Constants.WS_KEY_LOGIN_SERVICE,username,password);
            wsLogin.execute();
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

    private void showProgress(final boolean show) {

        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean>  {

        private Integer webServiceOperation;
        private String username;
        private String password;
        private String text;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
        }
        private AsyncCallWS(Integer wsOperation,String wsUsername, String wsPassword) {
            webServiceOperation = wsOperation;
            username = wsUsername;
            password = wsPassword;
        }

        private AsyncCallWS(Integer wsOperation,String wsText) {
            webServiceOperation = wsOperation;
            text = wsText;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            switch (webServiceOperation) {
                case Constants.WS_KEY_PUBLIC_TEST:
                    SoapServices.calculate(text);
                    validOperation = true;
                    break;
                case Constants.WS_KEY_LOGIN_SERVICE:
                    soapObject = SoapServices.checkUser(username,password);
                    Integer id = Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID).toString());

                    validOperation = (id > 0) ?  true : false;
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
            showProgress(false);
            if(success) {

                if (webServiceOperation == Constants.WS_KEY_LOGIN_SERVICE) {
                    //Official Login To Navigation Drawer//
                    Intent intentNavigationDrawer = new Intent(MainActivity.this,NavigationDrawerActivity.class);

                    if(Integer.valueOf(soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_ID_TEAM).toString()) == 0) {
                        Toast.makeText(MainActivity.this,getString(R.string.no_user_team_login_error), Toast.LENGTH_LONG).show();
                        return;
                    }

                    SoapObject location = (SoapObject) soapObject.getProperty(Constants.SOAP_OBJECT_KEY_LOGIN_TEAM_LOCATION);

                    Users user = new Users();

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

                    usernameLogin.clearFocus();
                    passwordLogin.clearFocus();

                    intentNavigationDrawer.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, user);
                    cleanAllLogin();
                    startActivity(intentNavigationDrawer);
                } else {
                    Toast.makeText(MainActivity.this, "TEST DE WEB", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(MainActivity.this,getString(R.string.default_login_error), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            super.onCancelled();
        }
    }
}
