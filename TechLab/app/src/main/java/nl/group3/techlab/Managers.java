package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import nl.group3.techlab.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import nl.group3.techlab.adapters.ManagerAdapter;
import nl.group3.techlab.adapters.ReturnItemAdapter;
import nl.group3.techlab.helpers.JSONHelper;

public class Managers extends MenuActivity {
    SharedPreferences sharedPreferences;
    ArrayList<JsonObject> managers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Techlab", 0);

        int d_color = sharedPreferences.getInt("d_color", 1);
        switch (d_color) {
            case 1:
                setTheme(R.style.theme1);
                break;
            case 2:
                setTheme(R.style.theme2);
                break;
            default:
                break;
        }
        setContentView(R.layout.activity_managers);
        super.onCreateDrawer();
        setTitle(R.string.title_activity_managers);

        managers = new ArrayList<>();

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                    String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/managers/"), null, 1000, "GET", null);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    for(JsonElement elem : jsonArray){
                        JsonObject obj = elem.getAsJsonObject();
                        managers.add(obj);
                    }

                }catch(Exception ex){ ex.printStackTrace();}
            }
        });
        // Start the new thread and run the code.
        thread.start();

        // Join the thread when it's done, meaning that the application will wait untill the
        // thread is done.
        try {
            thread.join();

        } catch (Exception ex){}

        ListView lv = findViewById(R.id.listViewManagers);
        lv.setAdapter(new ManagerAdapter(getBaseContext(), managers));

        ((FloatingActionButton) findViewById(R.id.addButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Managers.this, AddNewManager.class);
                startActivity(intent);
                finish();

            }
        });

    }

}
