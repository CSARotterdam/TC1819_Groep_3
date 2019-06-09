package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import nl.group3.techlab.adapters.ProductListAdapter;
import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.Writer;

public class ItemsAndMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button sign_out;
    GoogleSignInClient mGoogleSignInClient;
    static TextView emailTV;
    static TextView rolTV;
    String personEmail;
    BorrowDatabase db;
    boolean isManager;

    DatabaseHelper myDB;
    ListView listView;
    Item item;
    FloatingActionButton addProductButton;
    Menu nav_Menu;

    ArrayList<Book> books;
    public String[] arrayManagers;

    private static final String TAG = "ProductListAdapter";
    SharedPreferences sharedPreferences;
    ProductListAdapter productListAdapter;

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
        setContentView(R.layout.activity_items_and_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.Producten);

        LoginActivity.logged_in = true;

        // Dit zorgt ervoor dat de email in de header is.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        emailTV = (TextView) headerView.findViewById(R.id.emailtv);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ItemsAndMenuActivity.this);

        if (acct != null){
            String personName = acct.getDisplayName();
            personEmail = acct.getEmail();
        } else {
            personEmail = LoginActivity.Email;
        }
        emailTV.setText(personEmail);

        rolTV = (TextView) headerView.findViewById(R.id.rol);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        addProductButton = (FloatingActionButton) findViewById(R.id.addButton);

        if (!(personEmail.equalsIgnoreCase("techlabapp00@gmail.com"))) {
            Thread thread;
            thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/managers/", null, 5000, "GET", null);
                        Log.d("JSON", jsonString);

                        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                        arrayManagers = new String[jsonArray.size()];
                        for (int i = 0; i < jsonArray.size(); i++) {
                            arrayManagers[i] = jsonArray.get(i).getAsJsonObject().get("email").toString();
                            Log.d("JSON", arrayManagers[i]);
//                            if (arrayManagers[i].equals(personEmail)) { isManager = true; }
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

            nav_Menu.findItem(R.id.statistieken).setVisible(false);
            nav_Menu.findItem(R.id.beheerders).setVisible(false);



            List<String> list = Arrays.asList(arrayManagers);
            isManager = list.contains("\"" + personEmail + "\"");
            Log.d("JSON", personEmail);
            if (isManager) {
                rolTV.setText(getString(R.string.beheerder));
                nav_Menu.findItem(R.id.terugnemen).setVisible(true);
                addProductButton.show();
//                ItemEdit.delButton.show();
            } else {
                rolTV.setText(getString(R.string.gebruiker));
                nav_Menu.findItem(R.id.terugnemen).setVisible(false);
                addProductButton.hide();
//                ItemEdit.delButton.hide();
            }
        } else {
            rolTV.setText(getString(R.string.admin));
            nav_Menu.findItem(R.id.statistieken).setVisible(true);
            nav_Menu.findItem(R.id.beheerders).setVisible(true);
            nav_Menu.findItem(R.id.terugnemen).setVisible(true);
            addProductButton.show();
//            ItemEdit.delButton.show();
        }
        // Dit is voor de menu-button.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // dit is voor de items en het lenen
//        db = new BorrowDatabase(this);
//
//        myDB = new DatabaseHelper(this);
//
//        Cursor data = myDB.getListContents();
//        int numRows = data.getCount();
//        if (numRows == 0) {
////            Toast.makeText(ItemsAndMenuActivity.this, "Database is empty", Toast.LENGTH_LONG).show();
//        } else {
//            while (data.moveToNext()) {
//                item = new Item(data.getString(1), data.getString(2), data.getString(3), data.getInt(4));
//                itemList.add(item);
//            }
//            ProductListAdapter adapter = new ProductListAdapter(this, R.layout.content_adapter_view, itemList);
//            listView = findViewById(R.id.listView);
//            listView.setAdapter(adapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
//                    Item item = (Item) adapterView.getItemAtPosition(i);
//                    int quantity = item.getQuantity();
//                    String itemText = item.getName();
//                    String itemDesc = item.getDescription();
//
//                    Cursor data = myDB.getItemID(itemText);
//                    int ID = -1;
//
//                    while (data.moveToNext()) {
//                        ID = data.getInt(0);
//                    }
//                    if (ID > -1) {
//                        Log.d(TAG, "onItemClick: The ID is: " + ID);
//                        Intent editScreenIntent = new Intent(ItemsAndMenuActivity.this, ItemEdit.class);
//                        editScreenIntent.putExtra("id", ID);
//                        editScreenIntent.putExtra("quantity", quantity);
//                        editScreenIntent.putExtra("ITEM", itemText);
//                        editScreenIntent.putExtra("Description", itemDesc);
//                        startActivity(editScreenIntent);
//                        finish();
////                    } else{
////                        Toast.makeText(view.getContext(), "No ID associated with that name",
////                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsAndMenuActivity.this, AddNewItem.class);
                startActivity(intent);
                finish();
            }
        });

        loadItems();

        productListAdapter = new ProductListAdapter(this, R.layout.content_adapter_view, books);

        listView = findViewById(R.id.listView);
        listView.setAdapter(productListAdapter);


    }

    public void loadItems(){

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/items/", null, 5000, "GET", null);
                    Log.d("JSON", jsonString);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();

                    books = new ArrayList<Book>();

                    for(JsonElement elem : jsonArray){
                        JsonObject obj = elem.getAsJsonObject();
                        Writer[] writers = null;
                        if (obj.get("type").getAsString().equalsIgnoreCase("Book")) {
                            if(obj.get("writers").getAsJsonArray().size() > 0){
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
                                    obj.get("description").getAsString().replace("\\n", System.getProperty ("line.separator")),
                                    obj.get("borrow_days").getAsInt(),
                                    (obj.get("image").isJsonNull() ? null : new URL(obj.get("image").getAsString())), // new URL(obj.get("image").toString())
                                    obj.get("title").getAsString(),
                                    writers,
                                    obj.get("isbn").getAsString(),
                                    obj.get("publisher").getAsJsonObject().get("name").getAsString(),
                                    obj.get("stock").getAsInt()));
                            }
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(productListAdapter != null)
                                productListAdapter.notifyDataSetChanged();
                        }
                    });
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
        }catch(Exception ex){
            loadItems();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items_and_menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.uitloggen) {
            LoginActivity.logged_in = false;
            signOut();
        } else if(id == R.id.geschiedenis){
            startActivity(new Intent(ItemsAndMenuActivity.this, History.class));
        } else if(id == R.id.meldingen){
            startActivity(new Intent(ItemsAndMenuActivity.this, Notifications.class));
        } else if (id == R.id.terugnemen) {
            startActivity(new Intent(ItemsAndMenuActivity.this, ReturnItemActivity.class));
        } else if(id == R.id.statistieken){
            startActivity(new Intent(ItemsAndMenuActivity.this, statistic.class));
        } else if(id == R.id.instellingen){
            startActivity(new Intent(ItemsAndMenuActivity.this, settings.class));
        } else if(id == R.id.contact){
            startActivity(new Intent(ItemsAndMenuActivity.this, contact.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AddNewProduct(View view){
        Intent intent = new Intent(ItemsAndMenuActivity.this, AddNewItem.class);
        startActivity(intent);
        finish();
    }
    private void signOut() {
        // Dit zorgt ervoor dat je uitlogt, een toast krijgt na het uitloggen en dat je terug gaat naar het loginscherm
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        LoginActivity.logged_in = false;
                        Toast.makeText(ItemsAndMenuActivity.this, getString(R.string.uitgelogd), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemsAndMenuActivity.this, LoginActivity.class));
                        finish();
                    }
                });

    }

}
