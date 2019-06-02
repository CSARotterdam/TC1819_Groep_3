package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;

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

    public String[] writersArray, publishersArray, manufacturersArray, categoryArray ,array;

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
        startActivity(new Intent(AddNewItem.this, ItemsAndMenuActivity.class));
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
        productName.setText(getString(R.string.titel));
        productIdISBN.setText(getString(R.string.isbn));

        writersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/writers/");
        publishersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/publishers/");

        Spinner bookWriters = (Spinner) findViewById(R.id.writers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, writersArray);
        adapterWriters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookWriters.setPopupBackgroundResource(R.color.colorPrimary);
        bookWriters.setAdapter(adapterWriters);

        final Spinner bookPublisher = (Spinner) findViewById(R.id.publisher_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, publishersArray);
        adapterPublishers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookPublisher.setPopupBackgroundResource(R.color.colorPrimary);
        bookPublisher.setAdapter(adapterPublishers);

        productIdISBN = (EditText) findViewById(R.id.book_ISBN);

        myDB = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            JsonObject json = new Gson().toJsonTree(new Book(
                                    "Book",
                                    UUID.randomUUID().toString(),
                                    ProductDescription.getText().toString(),
                                    Integer.parseInt(productBorrowDays.getText().toString()),
                                    null,
                                    productName.getText().toString(),
                                    null,
                                    productIdISBN.getText().toString(),
                                    bookPublisher.getSelectedItem().toString(),
                                    Integer.parseInt(productQuantity.getText().toString()))).getAsJsonObject();
//                            String jsonString = JSONHelper.postJSONStringFromURL("http://84.86.201.7:8000/admin/", json);

//                            Log.d("JSON", jsonString);
                            Log.d("JSON", json.toString());

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

                Intent intent = new Intent(AddNewItem.this, ItemsAndMenuActivity.class);
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
        productName.setText(getString(R.string.naam));
        productIdISBN.setText(getString(R.string.product_id));

        manufacturersArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/manufacturers/");
        categoryArray = getDataForSpinner("http://84.86.201.7:8000/api/v1/categories/");

        Spinner Manufacturers = (Spinner) findViewById(R.id.manufacturers_spinner);
        ArrayAdapter<String> adapterWriters = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, manufacturersArray);
        adapterWriters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Manufacturers.setPopupBackgroundResource(R.color.colorPrimary);
        Manufacturers.setAdapter(adapterWriters);

        final Spinner Category = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapterPublishers = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, categoryArray);
        adapterPublishers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
