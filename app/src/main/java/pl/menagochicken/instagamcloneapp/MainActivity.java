package pl.menagochicken.instagamcloneapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean logIn = true;

    private TextView textView;

    private Button logInButton;
    private Button switchButton;

    public void switchLogInSignUp(View view) {
        if (logIn) {
            Log.i("Switch Button", "Switch button pressed, switched to Sign up");
            logInButton.setText("Sign up");
            switchButton.setText("Or log in?");
            logIn = false;
        } else {
            Log.i("Switch Button", "Switch button pressed, switched to Log in");
            logInButton.setText("Log in");
            switchButton.setText("Or sign up?");
            logIn = true;
        }

    }

    public void logInSignUp(View view) {
        if (logIn) {
            Log.i("Log in button", "Log In button pressed.");
        } else {
            Log.i("Sign up button", "Sign Up button pressed.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.appTitle);
        textView.setText(R.string.app_name);

        logInButton = findViewById(R.id.logIn);
        switchButton = findViewById(R.id.switchButton);


    }
}
