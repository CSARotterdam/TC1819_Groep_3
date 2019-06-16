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

    ArrayList<JsonObject> items;

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

        items = new ArrayList<>();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddNewItem.class);
                startActivity(intent);
                finish();
            }
        });

        loadItems();

        productListAdapter = new ProductListAdapter(this, R.layout.content_adapter_view, items);

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

                items.add(obj);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    if (productListAdapter != null)
                        productListAdapter.notifyDataSetChanged();
                }
            });

        } catch(Exception ex){ ex.printStackTrace();}
    }

    public void AddNewProduct(View view){
        Intent intent = new Intent(HomeActivity.this, AddNewItem.class);
        startActivity(intent);
        finish();
    }

}

