package pl.menagochicken.instagamcloneapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //stworzenie potrzebnych zmiennych
    private boolean logInCheck = true;

    private TextView textView;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private Button logInButton;
    private Button switchButton;

    private ImageView logoImageView;

    private ConstraintLayout background;

    private Intent intent;

    private SharedPreferences preferences;

    //uzyskiwanie pozwolenia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }


    //wyświetlenie menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.shere) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getPhoto();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //przypisanie zmiennych
        textView = findViewById(R.id.appTitle);
        textView.setText(R.string.app_name);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        logInButton = findViewById(R.id.logIn);
        switchButton = findViewById(R.id.switchButton);

        logoImageView = findViewById(R.id.logo);

        background = findViewById(R.id.backgroundLayout);
        background.setOnClickListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //sprawdzenie czy jest zalogowany i przekierowanie na userlist
        if (ParseUser.getCurrentUser() != null) {
            goToUserListActivity();
        }

    }

    //zmiana funkcjonalności buttonu
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

    //zalogowanie lub zarejestrowanie uzytkowanika w bazie danych
    public void logInSignUp(View view) {
        if (logInCheck) {

            Log.i("Log in button", "Log In button pressed.");
            logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());

        } else {

            Log.i("Sign up button", "Sign Up button pressed.");
            newUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());

        }
    }

    //stworzenie nowego uzytkowanika w bazie danych
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

    //zalogwanie się do aplikacji
    public void logIn(final String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {

                    goToUserListActivity();

                } else {

                    Log.i("Failed", "Failed to Log in - " + e.toString());
                    Toast.makeText(MainActivity.this, "Something went wrong :( - Invalid username/password.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    //wyczyszczenie widoku po rejestracji
    public void clearView() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        logInButton.setText("Log in");
        switchButton.setText("Or sign up?");
        logInCheck = true;
    }

    //usunięcie klawiatury z widoku
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backgroundLayout || v.getId() == R.id.logo || v.getId() == R.id.appTitle) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    //przeniesienie nna listę użytkowników
    public void goToUserListActivity() {
        intent = new Intent(MainActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    //pobieranie zdjęcia
    public void getPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivity(intent);
    }
}

