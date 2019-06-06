package nl.group3.techlab;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.adapters.HistoryAdapter;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.RowListItem;
import nl.group3.techlab.models.Writer;

import java.util.ArrayList;


public class History extends AppCompatActivity {
    BorrowDatabase mydb;
    SharedPreferences sharedPreferences;

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
        setContentView(R.layout.borrow_list);
        setTitle(R.string.geschiedenis);

        ListView listView = (ListView)findViewById(R.id.List_view);

        mydb=new BorrowDatabase(this);
        final ArrayList<JsonObject> thelist = new ArrayList<>();


        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());

                    String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/borrowitems/?email=%s", acct.getEmail()), null, 1000, "GET", null);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    for(JsonElement elem : jsonArray){
                        JsonObject obj = elem.getAsJsonObject();
                        thelist.add(obj);
                    }
//                    String json = new Gson().toJson(books.get(0));
//                    Log.d("Found book:", json);

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


//        Cursor data = mydb.getListContents();
//        if (data.getCount() != 0 ){
//            while (data.moveToNext())
//            {
//                String Itemname = (data.getString(1));
//                String Description = (data.getString(2));
//                thelist.add(new RowListItem(Itemname,Description));
//            }
//        }

        historyAdapter = new HistoryAdapter(this,thelist);
        listView.setAdapter(historyAdapter);

    }
}




