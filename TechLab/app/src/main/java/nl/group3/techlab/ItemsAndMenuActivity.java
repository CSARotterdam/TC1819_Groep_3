package nl.group3.techlab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;

import nl.group3.techlab.adapters.ProductListAdapter;
import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.models.Item;

public class ItemsAndMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button sign_out;
    GoogleSignInClient mGoogleSignInClient;
    TextView emailTV;
    BorrowDatabase db;

    DatabaseHelper myDB;
    ArrayList<Item> itemList;
    ListView listView;
    Item item;
    FloatingActionButton addProductButton;

    private static final String TAG = "ProductListAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_and_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LoginActivity.logged_in = true;

        // Dit is voor de menu-button.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Dit zorgt ervoor dat de email in de header is.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        emailTV = (TextView) headerView.findViewById(R.id.emailtv);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ItemsAndMenuActivity.this);

        if (acct != null){
            final String personEmail;
            String personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            emailTV.setText(personEmail);
        } else {
            emailTV.setText(LoginActivity.Email);
        }

        // dit is voor de items en het lenen
        db = new BorrowDatabase(this);

        addProductButton =  findViewById(R.id.addButton);

        myDB = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        if (numRows == 0) {
//            Toast.makeText(ItemsAndMenuActivity.this, "Database is empty", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                item = new Item(data.getString(1), data.getString(2), data.getString(3), data.getInt(4));
                itemList.add(item);
            }
            ProductListAdapter adapter = new ProductListAdapter(this, R.layout.content_adapter_view, itemList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                    Item item = (Item) adapterView.getItemAtPosition(i);
                    int quantity = item.getQuantity();
                    String itemText = item.getName();
                    String itemDesc = item.getDescription();

                    Log.d(TAG, "onItemClick You clicked on " + itemText);

                    Cursor data = myDB.getItemID(itemText);
                    int ID = -1;

                    while (data.moveToNext()) {
                        ID = data.getInt(0);
                    }
                    if (ID > -1) {
                        Log.d(TAG, "onItemClick: The ID is: " + ID);
                        Intent editScreenIntent = new Intent(ItemsAndMenuActivity.this, ItemEdit.class);
                        editScreenIntent.putExtra("id", ID);
                        editScreenIntent.putExtra("quantity", quantity);
                        editScreenIntent.putExtra("ITEM", itemText);
                        editScreenIntent.putExtra("Description", itemDesc);
                        startActivity(editScreenIntent);
                        finish();
                    } else{
                        Toast.makeText(view.getContext(), "No ID associated with that name",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsAndMenuActivity.this, AddNewItem.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items_and_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signOut() {
        // Dit zorgt ervoor dat je uitlogt, een toast krijgt na het uitloggen en dat je terug gaat naar het loginscherm
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        LoginActivity.logged_in = false;
                        Toast.makeText(ItemsAndMenuActivity.this, "Succesfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemsAndMenuActivity.this, LoginActivity.class));
                        finish();
                    }
                });

    }

}
