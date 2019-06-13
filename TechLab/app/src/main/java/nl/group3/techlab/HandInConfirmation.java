package nl.group3.techlab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

import nl.group3.techlab.database.ItemDatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.BorrowItem;
import nl.group3.techlab.models.StockItem;

public class HandInConfirmation extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int intAV;
    int intLE;
    int inttot;

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


        setContentView(R.layout.activity_hand_in_confirmation);


        final JsonObject borrowedItem = (JsonObject) new JsonParser().parse(getIntent().getStringExtra("BorrowedItemObject")).getAsJsonObject();

        TextView tvBorrowedBy = (TextView) findViewById(R.id.borrowedBy);
        final TextView tvItemName = (TextView) findViewById(R.id.itemName);

        final Button bCancelAction = (Button) findViewById(R.id.cancelAction);
        Button bReturnedAction = (Button) findViewById(R.id.returnAction);
        final Button bBrokenAction = (Button) findViewById(R.id.brokenAction);

        tvBorrowedBy.setText(String.format(tvBorrowedBy.getText().toString(),
                borrowedItem.get("user").getAsJsonObject().get("email")));

        tvItemName.setText(borrowedItem.get("item").getAsJsonObject().get("name").getAsString());

        bCancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent());

                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                startActivity(i);

                finish();
            }
        });

        bBrokenAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {

                            String jsonString = JSONHelper.JSONStringFromURL(String.format("http://84.86.201.7:8000/api/v1/returnitems/%s/", borrowedItem.get("id").getAsString()),
                                    new HashMap<String, String>(){{put("broken", "True");}}, 1000, "PUT", null);
                            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();


                            if (obj.get("success").getAsString().equalsIgnoreCase("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), R.string.product_teruggebracht, Toast.LENGTH_LONG).show();
                                    }
                                });

                                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                                startActivity(i);

                                finish();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                // Start the new thread and run the code.
                thread.start();

                // Join the thread when it's done, meaning that the application will wait untill the
                // thread is done.
                try {
                    thread.join();
                } catch (Exception ex) {
                }

            }
        });

        bReturnedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {

                            String jsonString = JSONHelper.JSONStringFromURL(String.format("http://84.86.201.7:8000/api/v1/returnitems/%s/", borrowedItem.get("id").getAsString()),
                                    new HashMap<String, String>(){{put("broken", "False");}}, 1000, "PUT", null);
                            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();


                            if (obj.get("success").getAsString().equalsIgnoreCase("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), R.string.product_teruggebracht, Toast.LENGTH_LONG).show();
                                    }
                                });

                                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                                startActivity(i);

                                finish();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                // Start the new thread and run the code.
                thread.start();

                // Join the thread when it's done, meaning that the application will wait untill the
                // thread is done.
                try {
                    thread.join();
                } catch (Exception ex) {
                }

            }
        });
    }
}
