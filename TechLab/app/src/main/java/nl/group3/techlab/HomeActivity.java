package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.ArrayList;

import nl.group3.techlab.adapters.ProductListAdapter;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Writer;

public class HomeActivity extends MenuActivity {
    ListView listView;
    FloatingActionButton addProductButton;

    ArrayList<Book> books;

    SharedPreferences sharedPreferences;
    ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Check for storage permission on opening application.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
//        }

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
//        int language = sharedPreferences.getInt("language", 1);
        int language = sharedPreferences.getInt("language", 0);
        switch (language) {
            case 1:
                setLocale("nl");
                break;
            case 2:
                setLocale("en");
                break;
            default:
                break;
        }
        setContentView(R.layout.activity_home);
        super.onCreateDrawer();
        setTitle(R.string.thuis);

        LoginActivity.logged_in = true;

        addProductButton = (FloatingActionButton) findViewById(R.id.addButton);

        if (MenuActivity.rolTV.getText() == getString(R.string.gebruiker)) {
            addProductButton.hide();
        } else {
            addProductButton.show();
        }

        books = new ArrayList<>();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddNewItem.class);
                startActivity(intent);
                finish();
            }
        });

        while(books.size() == 0)
            loadItems();

        productListAdapter = new ProductListAdapter(this, R.layout.content_adapter_view, books);

        listView = findViewById(R.id.listView);
        listView.setAdapter(productListAdapter);


    }

    public void loadItems(){
        try {
            String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/items/", null, 5000, "GET", null);
            Log.d("JSON", jsonString);

            JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

            for (JsonElement elem : jsonArray) {
                JsonObject obj = elem.getAsJsonObject();
                Writer[] writers = null;
                if (obj.get("type").getAsString().equalsIgnoreCase("Book")) {
                    if (obj.get("writers").getAsJsonArray().size() > 0) {
                        writers = new Writer[obj.get("writers").getAsJsonArray().size()];
                        for (int i = 0; i < obj.get("writers").getAsJsonArray().size(); i++) {
                            JsonObject writerObj = obj.get("writers").getAsJsonArray().get(i).getAsJsonObject();
                            writers[i] = (new Writer(writerObj.get("id").getAsInt(),
                                    writerObj.get("name").getAsString()));
                        }
                    }

                    books.add(new Book(
                            obj.get("type").getAsString(),
                            obj.get("id").getAsString(),
                            obj.get("description").getAsString().replace("\\n", System.getProperty("line.separator")),
                            obj.get("borrow_days").getAsInt(),
                            (obj.get("image").isJsonNull() ? null : new URL(obj.get("image").getAsString())), // new URL(obj.get("image").toString())
                            obj.get("name").getAsString(),
                            writers,
                            obj.get("isbn").getAsString(),
                            obj.get("publisher").getAsJsonObject().get("name").getAsString(),
                            obj.get("stock").getAsInt(),
                            obj.get("broken").getAsInt()));
                }
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    if (productListAdapter != null)
                        productListAdapter.notifyDataSetChanged();
                }
            });
            Log.d("Found books:", books.size() + "");

        } catch(Exception ex){ ex.printStackTrace();}
    }

    public void AddNewProduct(View view){
        Intent intent = new Intent(HomeActivity.this, AddNewItem.class);
        startActivity(intent);
        finish();
    }

}

