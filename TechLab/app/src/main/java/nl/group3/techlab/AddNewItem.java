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
    public String[] writersArray, publishersArray, manufacturersArray, categoryArray ,array, arrayWriters;
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

    public String[] getDataForSpinner(final String urlString){
        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String jsonString = JSONHelper.getJSONStringFromURL(urlString);
                    Log.d("JSON", jsonString);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    array = new String[jsonArray.size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        array[i] = jsonArray.get(i).getAsJsonObject().get("name").toString();
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

        writersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/writers/");
        publishersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/publishers/");

        final Spinner bookWriters = (Spinner) findViewById(R.id.writers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (this, R.layout.crimson_spinner_item, writersArray);
        adapterWriters.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        bookWriters.setPopupBackgroundResource(R.color.colorPrimary);
        bookWriters.setAdapter(adapterWriters);

        final Spinner bookPublisher = (Spinner) findViewById(R.id.publisher_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (this, R.layout.crimson_spinner_item, publishersArray);
        adapterPublishers.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        bookPublisher.setPopupBackgroundResource(R.color.colorPrimary);
        bookPublisher.setAdapter(adapterPublishers);

        productIdISBN = (EditText) findViewById(R.id.book_ISBN);

        myDB = new DatabaseHelper(this);

        writers = new Writer[1];

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String jsonString = JSONHelper.getJSONStringFromURL("http://84.86.201.7:8000/api/v1/writers/");
                    Log.d("JSON", jsonString);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
                    arrayWriters = new String[jsonArray.size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        arrayWriters[i] = jsonArray.get(i).getAsJsonObject().get("name").toString();
                        Log.d("JSON", arrayWriters[i]);
                            if (arrayWriters[i].equals(bookWriters.getSelectedItem().toString())) {
                                writerID = Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("id").getAsString()); }
                    }
//                    writerID = jsonArray.size() + 1;


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
        writer = new Writer(writerID, bookWriters.getSelectedItem().toString());
        writers[0] = writer;

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            if(imageUrl != null && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/books/",
                                        new HashMap<String, String>(){{
                                            put("username", "admin"); // TODO: Replace
                                            put("borrow_days",productBorrowDays.getText().toString());
                                            put("description",ProductDescription.getText().toString());
                                            put("title",productName.getText().toString());
                                            put("writers", writer.getId() + ""); //TODO: faster way to get the correct writer?
                                            put("publisher", "1"); //TODO: get publisher
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
                                        put("title",productName.getText().toString());
                                        put("writers", writer.getId() + "");
                                        put("publisher", "1");
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

        manufacturersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/manufacturers/");
        categoryArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/categories/");

        Spinner Manufacturers = (Spinner) findViewById(R.id.manufacturers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (this, R.layout.crimson_spinner_item, manufacturersArray);
        adapterWriters.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        Manufacturers.setPopupBackgroundResource(R.color.colorPrimary);
        Manufacturers.setAdapter(adapterWriters);

        final Spinner Category = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (this,  R.layout.crimson_spinner_item, categoryArray);
        adapterPublishers.setDropDownViewResource(R.layout.crimson_spinner_dropdown_item);
        Category.setPopupBackgroundResource(R.color.colorPrimary);
        Category.setAdapter(adapterPublishers);

//        productIdISBN = (EditText) findViewById(R.id.book_ISBN);

//        myDB = new DatabaseHelper(this);

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                Thread thread;
//                thread = new Thread(new Runnable() {
//                    public void run() {
//                        try {
//                            JsonObject json = new Gson().toJsonTree(new Electronic(
//                                    "Electronic",
//                                    UUID.randomUUID().toString(),
//                                    ProductDescription.getText().toString(),
//                                    Integer.parseInt(productBorrowDays.getText().toString()),
//                                    null,
//                                    productName.getText().toString(),
//                                    null,
//                                    productIdISBN.getText().toString(),
//                                    bookPublisher.getSelectedItem().toString(),
//                                    Integer.parseInt(productQuantity.getText().toString()))).getAsJsonObject();
////                            String jsonString = JSONHelper.postJSONStringFromURL("http://84.86.201.7:8000/admin/", json);
//
////                            Log.d("JSON", jsonString);
//                            Log.d("JSON", json.toString());
//
//                        }catch(Exception ex){ ex.printStackTrace();}
//                    }
//                });
//                // Start the new thread and run the code.
//                thread.start();
//
//                // Join the thread when it's done, meaning that the application will wait untill the
//                // thread is done.
//                try {
//                    thread.join();
//                }catch(Exception ex){ ex.printStackTrace();}

//        Intent intent = new Intent(AddNewItem.this, ItemsAndMenuActivity.class);
//        startActivity(intent);
//        finish();
//            }
//        });
    }
}
