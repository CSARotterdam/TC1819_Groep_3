package nl.group3.techlab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Writer;

public class AddNewManager extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        setContentView(R.layout.activity_new_manager);
        setTitle(R.string.title_activity_managers);

        ((Button)findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etEmail = (EditText) findViewById(R.id.email);

                if(etEmail.getText().length() != 0 && etEmail.getText().toString().contains("@hr.nl")){

                    Thread thread;
                    thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                                String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/managers/"), new HashMap<String, String>(){{put("email", etEmail.getText().toString()); }}, 1000, "POST", null);

                                final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
                                if(jsonObject.get("success").getAsBoolean()){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    finish();
                                }

                            }catch(Exception ex){
                                ex.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Something went wrong, please try again later.", Toast.LENGTH_LONG).show();

                                    }
                                });
                                finish();
                            }
                        }
                    });
                    // Start the new thread and run the code.
                    thread.start();

                    // Join the thread when it's done, meaning that the application will wait untill the
                    // thread is done.
                    try {
                        thread.join();

                    } catch (Exception ex){}

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddNewManager.this, HomeActivity.class));
        finish();
    }
}
