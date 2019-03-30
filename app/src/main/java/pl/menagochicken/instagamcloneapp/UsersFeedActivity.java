package pl.menagochicken.instagamcloneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UsersFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feed);

        final LinearLayout linearLayout = findViewById(R.id.linearLayout);

        //pobranie zawartości intentu
        Intent intent = getIntent();
        String activeUsername = intent.getStringExtra("username");

        //ustwienie tytułu strony aplikacji
        setTitle(activeUsername);

        //pobranie wszsytkich zdjęć użytkownika
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        //select
        query.whereEqualTo("username", activeUsername);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT, //width
                                                ViewGroup.LayoutParams.WRAP_CONTENT //height
                                        ));

                                        imageView.setImageBitmap(bitmap);
                                        linearLayout.addView(imageView);
                                    }

                                }
                            });
                        }
                    }

                }
            }
        });


    }
}
