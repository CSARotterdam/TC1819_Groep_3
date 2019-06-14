package nl.group3.techlab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.adapters.HistoryAdapter;
import nl.group3.techlab.helpers.JSONHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class History extends MenuActivity {
    BorrowDatabase mydb;
    SharedPreferences sharedPreferences;
    ArrayList<JsonObject> borrowedItemsList;

    private HistoryAdapter historyAdapter;
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
        setContentView(R.layout.activity_history);
        super.onCreateDrawer();
        setTitle(R.string.geschiedenis);

        // Dit zorgt ervoor dat de email in de header is.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView rolTV = (TextView) headerView.findViewById(R.id.rol);

        // code voor de switch
        final Switch switchHistory = (Switch) findViewById(R.id.history_switch);
        switchHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchHistory.setText(getString(R.string.geschiedenis_iedereen));
                } else {
                    switchHistory.setText(getString(R.string.geschiedenis_jij));
                }
                getHistoryData(switchHistory.isChecked());
            }
        });

        if(rolTV.getText().toString().equalsIgnoreCase(getString(R.string.gebruiker))) {
            ((Switch)findViewById(R.id.history_switch)).setVisibility(View.GONE);
        }

        ListView listView = (ListView)findViewById(R.id.List_view);

//        mydb=new BorrowDatabase(this);

        borrowedItemsList = new ArrayList<>();
        getHistoryData(switchHistory.isChecked());

        historyAdapter = new HistoryAdapter(this,borrowedItemsList);
        listView.setAdapter(historyAdapter);

    }

    void getHistoryData(final boolean everyone) {
        borrowedItemsList.clear();

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                    String jsonStringReturned;
                    String jsonStringBorrowed;

                    jsonStringReturned = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/returnitems/?%s", acct != null ? !everyone ? "email=" + acct.getEmail() : "" : ""), null, 1000, "GET", null);
                    jsonStringBorrowed = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/borrowitems/?%s", acct != null ? !everyone ? "email=" + acct.getEmail() : "" : ""), null, 1000, "GET", null);

                    JsonArray jsonArrayReturned = new JsonParser().parse(jsonStringReturned).getAsJsonArray();
                    JsonArray jsonArrayBorrowed = new JsonParser().parse(jsonStringBorrowed).getAsJsonArray();

                    for(JsonElement elem : jsonArrayReturned){
                        JsonObject obj = elem.getAsJsonObject();
                        borrowedItemsList.add(obj);
                    }
                    for(JsonElement elem : jsonArrayBorrowed){
                        JsonObject obj = elem.getAsJsonObject();
                        borrowedItemsList.add(obj);
                    }

                    Log.d("HISTORY", borrowedItemsList.size() + " items found");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            historyAdapter.notifyDataSetChanged();
                        }
                    });
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
    }
}




