package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.Writer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ItemEdit extends AppCompatActivity {
    //TODO: CLEANUP, add delete functionality
    private static final String TAG = "ItemEdit";

    private Button Borrow;
    public static FloatingActionButton delButton;
    private TextView eItem, eItemD, Bsk;
    private TextView ID;
    EditText eItemdes, eItemcat;
    DatabaseHelper myDB;
    private String selectedName;
    private int selectedID;
    BorrowDatabase db;

    private int selectedquan;
    private String selectedDesc;
    static int loanQuantity;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int intAV;
    int intLE;
    int inttot;


    @Override
    protected void onCreate(@Nullable Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
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
        setContentView(R.layout.borrow_item_layout);
        setTitle(R.string.lenen);
//        vBorrow = (Button) findViewById(R.id.vBorrow);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intAV = sharedPreferences.getInt("AV", 0);
        intLE = sharedPreferences.getInt("LE", 0);
        inttot = sharedPreferences.getInt("TOT", 0);

        db = new BorrowDatabase(this);
        ArrayList<Item> theList = new ArrayList<>();
        Cursor data = db.getListContents();

        Borrow = (Button) findViewById(R.id.Borrow);
        eItem = (TextView) findViewById(R.id.eItem);
        eItemD = (TextView) findViewById(R.id.eItemD);
        Bsk = (TextView) findViewById(R.id.Bsk);

        Intent receivedIntent = getIntent();

        final JsonObject item = new JsonParser().parse(receivedIntent.getSerializableExtra("item").toString()).getAsJsonObject();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

        delButton = (FloatingActionButton) findViewById(R.id.delButton);
        if (MenuActivity.rolTV.getText() == getString(R.string.gebruiker)) {
            delButton.hide();
        } else {
            delButton.show();
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                    Thread thread;
                    thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                String jsonString = JSONHelper.JSONStringFromURL(String.format("http://84.86.201.7:8000/api/v1/items/%s/", item.get("id").getAsString()),
                                        new HashMap<String, String>() {{
                                            put("username", acct.getEmail());
                                        }}
                                        , 1000, "DELETE", null);

                                Log.d("JSON", jsonString);

                                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();

                                Log.d("JSON", obj.toString());
                                obj.get("success").getAsBoolean();
                                Log.d("JSON", obj.get("success").getAsString() + "");
                                if (obj.get("success").getAsString().equalsIgnoreCase("true")) {
                                    onBackPressed();
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
                        ex.printStackTrace();
                    }

                }
            });
        }

        myDB = new DatabaseHelper(this);
        ID = (TextView) findViewById(R.id.ID);

        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("ITEM");
        selectedquan = receivedIntent.getIntExtra("quantity", 0);
        selectedDesc = receivedIntent.getStringExtra("Description");


        eItem.setText(item.get("name").getAsString());
        eItemD.setText(item.get("description").getAsString());
        {
            ViewGroup.LayoutParams params = eItemD.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            eItemD.setLayoutParams(params); // this call is what you need to add
        }
        if (item.get("stock").getAsInt() > 0) {
            Bsk.setText(R.string.beschikbaar);
        } else {
            Bsk.setText(R.string.niet_beshikbaar);
        }

        if(item.get("type").getAsString().contains("Book")){
            ((TextView)findViewById(R.id.manufacturers)).setVisibility(TextView.GONE);
            ((TextView)findViewById(R.id.category)).setVisibility(TextView.GONE);

            {
                String writers = "";
                if (!item.get("writers").isJsonNull()) {
                    for (JsonElement writer : item.get("writers").getAsJsonArray()) {
                        JsonObject obj = writer.getAsJsonObject();
                        writers += obj.get("name").getAsString() + ", ";
                    }
                    if (writers.length() > 0)
                        writers = writers.substring(0, writers.length() - 2);
                }

                ((TextView) findViewById(R.id.writers)).setText(String.format(((TextView) findViewById(R.id.writers)).getText().toString(), writers));
                ViewGroup.LayoutParams params = findViewById(R.id.writers).getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                findViewById(R.id.writers).setLayoutParams(params); // this call is what you need to add
            }

            {
                ((TextView) findViewById(R.id.publisher)).setText(String.format(((TextView) findViewById(R.id.publisher)).getText().toString(), item.get("publisher").getAsJsonObject().get("name").getAsString()));
                ViewGroup.LayoutParams params = findViewById(R.id.publisher).getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                findViewById(R.id.publisher).setLayoutParams(params); // this call is what you need to add
            }
        }

        if(item.get("type").getAsString().contains("Electronic")){
            ((TextView)findViewById(R.id.writers)).setVisibility(TextView.GONE);
            ((TextView)findViewById(R.id.publisher)).setVisibility(TextView.GONE);

//            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.BELOW, R.id.below_id);
//            viewToLayout.setLayoutParams(params);
//
//            ((TextView)findViewById(R.id.eItemD)).setLayoutParams(TextView.layout);
            {
                String manufacturers = "";
                if (!item.get("manufacturer").isJsonNull()) {
                    for (JsonElement writer : item.get("manufacturer").getAsJsonArray()) {
                        JsonObject obj = writer.getAsJsonObject();
                        manufacturers += obj.get("name").getAsString() + ", ";
                    }
                    if (manufacturers.length() > 0)
                        manufacturers = manufacturers.substring(0, manufacturers.length() - 2);
                }

                ((TextView) findViewById(R.id.manufacturers)).setText(String.format(((TextView) findViewById(R.id.manufacturers)).getText().toString(), manufacturers));
                ViewGroup.LayoutParams params = findViewById(R.id.manufacturers).getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                findViewById(R.id.manufacturers).setLayoutParams(params); // this call is what you need to add
            }

            {
                ((TextView) findViewById(R.id.category)).setText(String.format(((TextView) findViewById(R.id.category)).getText().toString(), item.get("category").getAsJsonObject().get("name").getAsString()));
                ViewGroup.LayoutParams params = findViewById(R.id.category).getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                findViewById(R.id.category).setLayoutParams(params); // this call is what you need to add
            }
        }

        try {
            if (!item.get("image").isJsonNull())
                ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(Item.getImage(getBaseContext(), new URL(item.get("image").getAsString())));
        } catch (Exception ex) { ex.printStackTrace(); }


        Borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/borrowitems/",
                                        new HashMap<String, String>() {{
                                            put("email", acct.getEmail());
                                            put("item_id", item.get("id").getAsString());
                                            put("borrow_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                        }}
                                        , 1000, "PUT", null);

                                Log.d("JSON", jsonString);

                                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();

                                Log.d("JSON", obj.toString());
                                obj.get("success").getAsBoolean();
                                Log.d("JSON", obj.get("success").getAsString() + "");
                                if (obj.get("success").getAsString().equalsIgnoreCase("true")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), R.string.product_geleend, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    onBackPressed();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), R.string.product_niet_beschikbaar, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
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
                    ex.printStackTrace();
                }
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void toastMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
