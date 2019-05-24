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
    public static final Locale ENGLISH = Locale.ENGLISH;
    public static final Locale NEDERLANDS = new Locale("de_NL");
    public static Locale DEFAULT = Locale.getDefault();

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
//                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (radioButton.isChecked()) {
//                    editor.putInt("d_color", 1);
                    setLocale(NEDERLANDS);
                }

                if (radioButton2.isChecked()) {
//                    editor.putInt("d_color", 2);
                    setLocale(ENGLISH);
                }

//                editor.apply();
                Intent intent = new Intent(v.getContext(), ItemsAndMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
//                finish();
            }

        });
    }
//    public void setLocale(String lang) {
//        Locale myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, ItemsAndMenuActivity.class);
//        startActivity(refresh);
//        finish();
//    }
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale){
//        SharedPrefUtils.saveLocale(locale); // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
}
