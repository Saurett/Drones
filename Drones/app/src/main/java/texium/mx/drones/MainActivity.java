package texium.mx.drones;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
            passwordLogin.setError("This password is invalid");
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameLogin.setError("This field is required");
            cancel = true;
        }

        if (cancel) {

        } else {
            Intent intentNavigationDrawer = new Intent(MainActivity.this,NavigationDrawerActivity.class);
            cleanAllLogin();
            startActivity(intentNavigationDrawer);
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
}
