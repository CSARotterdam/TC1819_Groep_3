package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;


public class settings extends MenuActivity {

    Button language, theme, notification;
    SharedPreferences sharedPreferences;
    public static boolean notificationOn;

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
        setContentView(R.layout.activity_settings);
        super.onCreateDrawer();
        setTitle(R.string.instellingen);
        final Switch notificationSwitch = (Switch) findViewById(R.id.switch1);
        int notifications = sharedPreferences.getInt("notifications", 1);
        switch (notifications) {
            case 1:
                notificationSwitch.setChecked(true);
                break;
            case 2:
                notificationSwitch.setChecked(false);
                break;

            default:
                break;
        }
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putInt("notifications", 1);
                    notificationOn = true;

                } else {
                    editor.putInt("notifications", 2);
                    notificationOn = false;
                }
                editor.apply();
            }

        });
        language = (Button) findViewById(R.id.language);
        theme = (Button) findViewById(R.id.theme);
        notification = (Button) findViewById(R.id.notification);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settings.this, language.class);
                startActivity(intent);
            }
        });

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settings.this, theme.class);
                startActivity(intent);
            }
        });



    }
}
