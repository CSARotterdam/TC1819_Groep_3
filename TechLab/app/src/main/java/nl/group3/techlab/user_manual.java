package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class user_manual extends MenuActivity {

    SharedPreferences sharedPreferences;
    TextView manualUser, manualManagerAdmin, manualAdmin;

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

        setContentView(R.layout.activity_user_manual);
        super.onCreateDrawer();
        setTitle(R.string.handleiding);

        manualManagerAdmin = (TextView) findViewById(R.id.handleidingB);
        manualAdmin = (TextView) findViewById(R.id.handleidingA);
        manualUser = (TextView) findViewById(R.id.handleidingG);

        if (MenuActivity.rolTV.getText() == getString(R.string.gebruiker)) {
            manualAdmin.setVisibility(View.GONE);
            manualManagerAdmin.setVisibility(View.GONE);
        } else {
            manualManagerAdmin.setVisibility(View.VISIBLE);
            manualAdmin.setVisibility(View.VISIBLE);
        }

    }




}
