package nl.group3.techlab;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Writer;

public class AddNewItem extends AppCompatActivity {
    EditText productName, productCategory, ProductDescription, productQuantity, productBorrowDays, productIdISBN;
    Button btnAdd;
    DatabaseHelper myDB;
    static int totalQuantity;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int intAV;
    int intLE;
    int inttot;
    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    String imageUrl;
//    public String[] writersArray, publishersArray, manufacturersArray, categoryArray, arrayWriters;
    HashMap<String, String> writersMap, publishersMap, manufacturersMap, categoriesMap;
    Writer[] writers;
    Writer writer;
    int writerID;


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

        setContentView(R.layout.add_item);
        setTitle(R.string.product_toevoegen);
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intAV = sharedPreferences.getInt("AV", 0);
        intLE = sharedPreferences.getInt("LE", 0);
        inttot = sharedPreferences.getInt("TOT", 0);

        productName = (EditText) findViewById(R.id.eItem);
        ProductDescription = (EditText) findViewById(R.id.eItemdes);
        productQuantity = (EditText) findViewById(R.id.eItemq);
        productBorrowDays = (EditText) findViewById(R.id.item_borrow_time);
        productIdISBN = findViewById(R.id.book_ISBN);
        btnAdd = (Button) findViewById(R.id.btnAdd);
//        Button bookButton = (Button) findViewById(R.id.book_button);
        AddBook((Button) findViewById(R.id.book_button));
        imageView = (ImageView) findViewById(R.id.imageview1);
        button = (Button)findViewById(R.id.Uploadimage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        writersMap = new HashMap<>();
        publishersMap = new HashMap<>();
        manufacturersMap = new HashMap<>();
        categoriesMap = new HashMap<>();

    }

    private void openGallery() {
        // Call the image gallery
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // To recognize when the user comes back from the image gallery and to display an error
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE ){
            // Getting image
            imageUri = data.getData();
            // Set the image in an image view
            imageView.setImageURI(imageUri);
            // To allow the user to use any gallery apps he might have installed
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getBaseContext().getContentResolver().query(imageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imageUrl = cursor.getString(column_index);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public void AddData(String Item, String Categorie, String Description, int fQuan){
        boolean insertData = myDB.addData(Item,Categorie,Description,fQuan);
        if (insertData){
            Toast.makeText(AddNewItem.this, getString(R.string.product_toegevoegd), Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(AddNewItem.this, getString(R.string.pruduct_toevoegen_mislukt), Toast.LENGTH_LONG).show();

        }
    }

    static public int getTotalQuantity(){
        return totalQuantity;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddNewItem.this, HomeActivity.class));
        finish();
    }

    public HashMap<String, String> getDataForSpinner(final String urlString){
        final HashMap<String, String> array = new HashMap<>();

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String jsonString = JSONHelper.JSONStringFromURL(urlString, null, 1000, "GET", null);
                    Log.d("JSON", jsonString);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        array.put(jsonArray.get(i).getAsJsonObject().get("name").getAsString(), jsonArray.get(i).getAsJsonObject().get("id").getAsString());
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
        }catch(Exception ex){ ex.printStackTrace();}

        return array;
    }

    public void AddBook(View view) {
        findViewById(R.id.writers_spinner).setVisibility(View.VISIBLE);
        findViewById(R.id.publisher_spinner).setVisibility(View.VISIBLE);
        findViewById(R.id.category_spinner).setVisibility(View.GONE);
        findViewById(R.id.manufacturers_spinner).setVisibility(View.GONE);
        productName.setHint(getString(R.string.titel));
        productIdISBN.setHint(getString(R.string.isbn));

        writersMap = getDataForSpinner("http://84.86.201.7:8000/api/v1/writers/");
        publishersMap = getDataForSpinner("http://84.86.201.7:8000/api/v1/publishers/");

        final Spinner bookWriters = (Spinner) findViewById(R.id.writers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (getBaseContext(), R.layout.crimson_spinner_item, writersMap.keySet().toArray(new String[writersMap.size()]));
        adapterWriters.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        bookWriters.setPopupBackgroundResource(R.color.colorPrimary);
        bookWriters.setAdapter(adapterWriters);

        final Spinner bookPublisher = (Spinner) findViewById(R.id.publisher_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (getBaseContext(), R.layout.crimson_spinner_item, publishersMap.keySet().toArray(new String[publishersMap.size()]));
        adapterPublishers.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        bookPublisher.setPopupBackgroundResource(R.color.colorPrimary);
        bookPublisher.setAdapter(adapterPublishers);

        productIdISBN = (EditText) findViewById(R.id.book_ISBN);

        myDB = new DatabaseHelper(this);

        writers = new Writer[1];

        final HashMap<String, String> finalWritersMap = new HashMap<>(writersMap);
        final HashMap<String, String> finalPublishersMap = new HashMap<>(publishersMap);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {

                            String w = "";
                            String p = "";
                            for (Map.Entry<String, String> item : finalWritersMap.entrySet()){
                                if(((Spinner) findViewById(R.id.writers_spinner)).getSelectedItem().toString() == item.getKey().toString()){
                                    w = item.getValue();
                                    break;
                                }
                            }
                            Log.d("AddNewItem w", w);

                            for (Map.Entry<String, String> item : finalPublishersMap.entrySet()){
                                if(((Spinner) findViewById(R.id.publisher_spinner)).getSelectedItem().toString() == item.getKey().toString()){
                                    p = item.getValue();
                                    break;
                                }
                            }
                            Log.d("AddNewItem p", p);


                            final String finalWriter = w;
                            final String finalPublisher = p;

                            if(imageUrl != null && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/books/",
                                        new HashMap<String, String>(){{
                                            put("username", "admin"); // TODO: Replace
                                            put("borrow_days",productBorrowDays.getText().toString());
                                            put("description",ProductDescription.getText().toString());
                                            put("name",productName.getText().toString());
                                            put("writers", finalWriter);
                                            put("publisher", finalPublisher);
                                            put("isbn", productIdISBN.getText().toString());
                                            put("stock", productQuantity.getText().toString()); }}

                                        , 15000, "POST", imageUrl);
                                Log.d("JSON", jsonString);
                            } else {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/books/",
                                    new HashMap<String, String>(){{
                                        put("username", "admin"); // TODO: Replace
                                        put("borrow_days",productBorrowDays.getText().toString());
                                        put("description",ProductDescription.getText().toString());
                                        put("name",productName.getText().toString());
                                        put("writers", finalWriter);
                                        put("publisher", finalPublisher);
                                        put("isbn", productIdISBN.getText().toString());
                                        put("stock", productQuantity.getText().toString()); }}

                                    , 15000, "POST", null);
                                Log.d("JSON", jsonString);
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
                }catch(Exception ex){ ex.printStackTrace();}

                Intent intent = new Intent(AddNewItem.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void AddElectronic(View view) {
        findViewById(R.id.writers_spinner).setVisibility(View.GONE);
        findViewById(R.id.publisher_spinner).setVisibility(View.GONE);
        findViewById(R.id.category_spinner).setVisibility(View.VISIBLE);
        findViewById(R.id.manufacturers_spinner).setVisibility(View.VISIBLE);
        productName.setHint(getString(R.string.naam));
        productIdISBN.setHint(getString(R.string.product_id));

        manufacturersMap = getDataForSpinner("http://84.86.201.7:8000/api/v1/manufacturers/");
        categoriesMap = getDataForSpinner("http://84.86.201.7:8000/api/v1/categories/");

        Spinner Manufacturers = (Spinner) findViewById(R.id.manufacturers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (getBaseContext(), R.layout.crimson_spinner_item, manufacturersMap.keySet().toArray(new String[manufacturersMap.size()]));
        adapterWriters.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        Manufacturers.setPopupBackgroundResource(R.color.colorPrimary);
        Manufacturers.setAdapter(adapterWriters);

        final Spinner Category = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (getBaseContext(),  R.layout.crimson_spinner_item, categoriesMap.keySet().toArray(new String[categoriesMap.size()]));
        adapterPublishers.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        Category.setPopupBackgroundResource(R.color.colorPrimary);
        Category.setAdapter(adapterPublishers);

        final HashMap<String, String> finalCategoriesMap = new HashMap<>(categoriesMap);
        final HashMap<String, String> finalManufacturersMap = new HashMap<>(manufacturersMap);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {

                            String c = "";
                            String m = "";

                            for (Map.Entry<String, String> item : finalCategoriesMap.entrySet()){
                                if(((Spinner) findViewById(R.id.category_spinner)).getSelectedItem().toString() == item.getKey().toString()){
                                    c = item.getValue();
                                    break;
                                }
                            }
                            Log.d("AddNewItem c", c);

                            for (Map.Entry<String, String> item : finalManufacturersMap.entrySet()){
                                if(((Spinner) findViewById(R.id.manufacturers_spinner)).getSelectedItem().toString() == item.getKey().toString()){
                                    m = item.getValue();
                                    break;
                                }
                            }
                            Log.d("AddNewItem m", m);


                            final String finalCategory = c;
                            final String finalManufacturer = m;

                            if(imageUrl != null && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/electronics/",
                                        new HashMap<String, String>(){{
                                            put("username", "admin"); // TODO: Replace
                                            put("borrow_days",productBorrowDays.getText().toString());
                                            put("description",ProductDescription.getText().toString());
                                            put("name",productName.getText().toString());
                                            put("product_id",productIdISBN.getText().toString());
                                            put("stock", productQuantity.getText().toString());
                                            put("manufacturer", finalManufacturer);
                                            put("category", finalCategory);}}

                                        , 15000, "POST", imageUrl);
                                Log.d("JSON", jsonString);
                            } else {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/electronics/",
                                        new HashMap<String, String>(){{
                                            put("username", "admin"); // TODO: Replace
                                            put("borrow_days",productBorrowDays.getText().toString());
                                            put("description",ProductDescription.getText().toString());
                                            put("name",productName.getText().toString());
                                            put("product_id",productIdISBN.getText().toString());
                                            put("stock", productQuantity.getText().toString());
                                            put("manufacturer", finalManufacturer);
                                            put("category", finalCategory);}}

                                        , 15000, "POST", null);
                                Log.d("JSON", jsonString);
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
                }catch(Exception ex){ ex.printStackTrace();}

                Intent intent = new Intent(AddNewItem.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
