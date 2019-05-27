package nl.group3.techlab;

import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.PriorityQueue;

public class Notifications extends AppCompatActivity {
private EditText editTextTitle;
private EditText editTextDescription;
private Button notify;
private  NotificationHelper mNotificationHelper;
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
        setContentView(R.layout.activity_notifications);
        setTitle(R.string.meldingen);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        notify = (Button)findViewById(R.id.notify);
        mNotificationHelper = new NotificationHelper(this);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOnChannel(editTextTitle.getText().toString(),editTextDescription.getText().toString());
            }
        });
    }

    public void SendOnChannel(String title, String description) {
        NotificationCompat.Builder nb = mNotificationHelper.getCahnnelNotification(title, description);
        mNotificationHelper.getMAnager().notify(1,nb.build());
    }

}
