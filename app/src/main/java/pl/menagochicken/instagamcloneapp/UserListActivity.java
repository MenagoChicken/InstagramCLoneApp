package pl.menagochicken.instagamcloneapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private List<String> usernames;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

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
                if (e == null){

                    if (objects.size() > 0){

                        //przepisanie nazw użytkowników do listy
                        for (ParseUser user : objects){

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
}
