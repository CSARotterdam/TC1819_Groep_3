package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.gson.JsonParser;

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

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        DrawerLayout drawer;

        ArrayList<Book> books;
        public String[] arrayManagers;

        private static final String TAG = "ProductListAdapter";
        SharedPreferences sharedPreferences;
        ProductListAdapter productListAdapter;

//        @Override
        protected void onCreateDrawer() {
//            super.onCreate(savedInstanceState);

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
            drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

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
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MenuActivity.this);

            if (acct != null) {
                String personName = acct.getDisplayName();
                personEmail = acct.getEmail();
            } else {
                personEmail = LoginActivity.Email;
            }
            emailTV.setText(personEmail);

            rolTV = (TextView) headerView.findViewById(R.id.rol);

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
//            addProductButton = (FloatingActionButton) findViewById(R.id.addButton);

//            books = new ArrayList<>();

            // Dit is voor de menu-button.
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
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
            } else if(id == R.id.thuis){
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
            }else if(id == R.id.geschiedenis){
                startActivity(new Intent(MenuActivity.this, History.class));
            } else if(id == R.id.meldingen){
                startActivity(new Intent(MenuActivity.this, Notifications.class));
            } else if (id == R.id.terugnemen) {
                startActivity(new Intent(MenuActivity.this, ReturnItemActivity.class));
            } else if(id == R.id.statistieken){
                startActivity(new Intent(MenuActivity.this, statistic.class));
            } else if(id == R.id.instellingen){
                startActivity(new Intent(MenuActivity.this, settings.class));
            } else if(id == R.id.contact){
                startActivity(new Intent(MenuActivity.this, contact.class));
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
                        Toast.makeText(MenuActivity.this, getString(R.string.uitgelogd), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                        finish();
                    }
                });

    }

}
