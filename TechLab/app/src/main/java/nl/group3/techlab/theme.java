package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class theme extends AppCompatActivity {
    SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_theme);
        Button button = findViewById(R.id.button);
        final RadioButton radioButton = findViewById(R.id.rbc1);
        final RadioButton radioButton2 = findViewById(R.id.rbc2);
        switch (d_color) {
            case 1:
                radioButton.setChecked(true);
                break;
            case 2:
                radioButton2.setChecked(true);
                break;

            default:
                break;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (radioButton.isChecked()) {
                    editor.putInt("d_color", 1);
                }

                if (radioButton2.isChecked()) {
                    editor.putInt("d_color", 2);
                }

                editor.apply();
                Intent intent = new Intent(v.getContext(), ItemsAndMenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
//                finish();
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(theme.this, ItemsAndMenuActivity.class);
        startActivity(intent);
    }
}
