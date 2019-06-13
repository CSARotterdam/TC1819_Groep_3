package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Locale;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class language extends AppCompatActivity {
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

        setContentView(R.layout.activity_language);
        setTitle(R.string.taal);
        Button button = findViewById(R.id.button);
        int language = sharedPreferences.getInt("language", 0);
        final RadioButton radioButton = findViewById(R.id.rbc1);
        final RadioButton radioButton2 = findViewById(R.id.rbc2);
        switch (language) {
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
                    editor.putInt("language", 1);
                }

                if (radioButton2.isChecked()) {
                    editor.putInt("language", 2);
                }

                editor.apply();
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
//                finish();
            }

        });
    }
}
