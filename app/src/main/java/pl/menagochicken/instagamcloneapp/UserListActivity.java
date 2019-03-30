package pl.menagochicken.instagamcloneapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private List<String> usernames;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

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

    //pobranie zdjęcia z galerii i zapis do servera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            //pobranie wybranego zdjęcia
            Uri selectedImage = data.getData();

            try {
                //zapisanie do bitmapy
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Log.i("Photo", "Pobrane");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                //stworzenie nowego pliku bytowego
                ParseFile file = new ParseFile("image.png", byteArray);

                //stworzenie nowej klasy
                ParseObject object = new ParseObject("Image");

                //umieszczenie obiektu
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());

                //zapis w tle
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(UserListActivity.this, "Image shared!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserListActivity.this, "Image not shared :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("TAG", "Some exception " + e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("TAG2", "Some exception " + e);
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

    //wybiernaie opcji z menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.shere) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getPhoto();
            }
        }
//wylogowanie
        if (item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // przypisanie zmiennych
        usernames = new ArrayList<>();

        listView = findViewById(R.id.userListView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);

        //zapytanie do servera danych o listę użytowników
        ParseQuery<ParseUser> userListQuery = ParseUser.getQuery();

        //zapytanie do bazy o listę użytkowników nie będącym zalogowanym użytkownikiem
        userListQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        //ułożenie w porządku alfabetycznym
        userListQuery.addAscendingOrder("username");

        //proces zandowania danych w tle
        userListQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {

                        //przepisanie nazw użytkowników do listy
                        for (ParseUser user : objects) {

                            usernames.add(user.getUsername());

                        }
                        //przypisanie adaptera do listy
                        listView.setAdapter(arrayAdapter);
                    }

                } else {

                    Log.i("ERROR", e.toString());

                }
            }
        });

    }

    //pobieranie zdjęcia
    public void getPhoto() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }
}
