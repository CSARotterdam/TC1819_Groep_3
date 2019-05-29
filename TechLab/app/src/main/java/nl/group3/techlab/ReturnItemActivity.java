package nl.group3.techlab;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import nl.group3.techlab.adapters.ReturnItemAdapter;
import nl.group3.techlab.database.ItemDatabaseHelper;
import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.BorrowItem;

public class ReturnItemActivity extends AppCompatActivity {
    ArrayList<BorrowItem> borrowedItems;
    ArrayList<Book> books;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Techlab", 0);
        int d_color = sharedPreferences.getInt("d_color", 1);


        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String jsonString = JSONHelper.getJSONStringFromURL("http://84.86.201.7:8000/api/v1/items/");
                    Log.d("JSON", jsonString);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    books = new ArrayList<Book>();

                    for(JsonElement elem : jsonArray){
                        JsonObject obj = elem.getAsJsonObject();

                        if (obj.get("type").getAsString().equalsIgnoreCase("Book")){
                            books.add(new Book(
                                    obj.get("type").toString(),
                                    obj.get("id").toString(),
                                    obj.get("description").toString(),
                                    Integer.parseInt(obj.get("borrow_days").toString()),
                                    null, // new URL(obj.get("image").toString())
                                    obj.get("title").toString(),
                                    null,
                                    obj.get("isbn").toString(),
                                    obj.get("publisher_id").toString(), // TODO: Get the publishers name
                                    Integer.parseInt(obj.get("stock").toString())));
                        } else {
                            Log.d("Books", "Failed to find a book");
                        }

                        Log.d("JSON", obj.get("type").toString());
                    }
//                    String json = new Gson().toJson(books.get(0));
//                    Log.d("Found book:", json);

                    Log.d("Found books:", books.size() + "");

                }catch(Exception ex){ ex.printStackTrace();}
            }
        });
        // Start the new thread and run the code.
        thread.start();

        // Join the thread when it's done, meaning that the application will wait untill the
        // thread is done.
        try {
            thread.join();
        }catch(Exception ex){ ex.printStackTrace();}

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
        setContentView(R.layout.activity_hand_in);
        setTitle(R.string.terugnemen);

        LoadDatabaseData();

        ListView lv = findViewById(R.id.listViewBorrowItem);
        lv.setAdapter(new ReturnItemAdapter(getBaseContext(), borrowedItems));

    }
    public void LoadDatabaseData(){
        ItemDatabaseHelper myDB = new ItemDatabaseHelper(this);
        BorrowDatabase yourDB = new BorrowDatabase(this);
        // borrowedItems = new ArrayList<BorrowItem>(Arrays.asList(myDB.getBorrowedItems(new User(1, "John", "Doe"), false)));



        borrowedItems = new ArrayList<BorrowItem>();
//        for(BorrowItem item : borrowedItems){
//            ArrayList<StockItem> stock = new ArrayList<StockItem>(Arrays.asList(myDB.getStockItem(item.getItem())));
//            Log.d("ReturnItemActivity", stock.get(0).getItem().getName() + " - " + stock.get(0).getStock() + " - " + stock.get(0).getBroken());
//        }
        Cursor data = yourDB.getListContents();

        while (data.moveToNext()) {
            Log.d("ReturnItemActivity", data.toString());
            int id = data.getInt(0);
            String name = data.getString(1);
            String desc = data.getString(2);

            borrowedItems.add(new BorrowItem(id, name, desc));
        }



    }

}
