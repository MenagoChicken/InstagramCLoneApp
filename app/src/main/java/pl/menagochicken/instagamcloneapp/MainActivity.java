package pl.menagochicken.instagamcloneapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean logInCheck = true;

    private TextView textView;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private Button logInButton;
    private Button switchButton;

    public void switchLogInSignUp(View view) {
        if (logInCheck) {
            Log.i("Switch Button", "Switch button pressed, switched to Sign up");
            logInButton.setText("Sign up");
            switchButton.setText("Or log in?");
            logInCheck = false;
        } else {
            Log.i("Switch Button", "Switch button pressed, switched to Log in");
            logInButton.setText("Log in");
            switchButton.setText("Or sign up?");
            logInCheck = true;
        }

    }

    public void logInSignUp(View view) {
        if (logInCheck) {
            Log.i("Log in button", "Log In button pressed.");
            logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        } else {
            Log.i("Sign up button", "Sign Up button pressed.");
            newUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.appTitle);
        textView.setText(R.string.app_name);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        logInButton = findViewById(R.id.logIn);
        switchButton = findViewById(R.id.switchButton);

    }

    public void newUser(final String username, String password) {

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Sign up --- Welcome " + username, Toast.LENGTH_LONG).show();
                    clearView();
                } else {
                    Log.i("Failed", "Failed to Sign up - " + e.toString());
                    Toast.makeText(MainActivity.this, "Something went wrong :( - Account already exists for this username.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void logIn(final String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Log in --- Welcome " + username, Toast.LENGTH_LONG).show();
                } else {
                    Log.i("Failed", "Failed to Log in - " + e.toString());
                    Toast.makeText(MainActivity.this, "Something went wrong :( - Invalid username/password.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void clearView() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        logInButton.setText("Log in");
        switchButton.setText("Or sign up?");
        logInCheck = true;
    }
}

