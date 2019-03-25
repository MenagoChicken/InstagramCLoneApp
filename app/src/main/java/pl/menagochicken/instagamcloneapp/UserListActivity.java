package pl.menagochicken.instagamcloneapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private List<String> userList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userList = new ArrayList<>();
        userList.add("Michal");
        userList.add("Marta");
        userList.add("Marcin");
        userList.add("Michal");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        listView = findViewById(R.id.userListView);

        listView.setAdapter(arrayAdapter);
    }
}
