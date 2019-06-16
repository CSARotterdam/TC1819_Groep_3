package nl.group3.techlab;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import nl.group3.techlab.helpers.JSONHelper;

public class Notifications extends MenuActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button notify;
    Thread thread;
    private  NotificationHelper mNotificationHelper;
    SharedPreferences sharedPreferences;
    ArrayList<JsonObject> borrowedItems;
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
        int notifications = sharedPreferences.getInt("notifications", 1);
        switch (notifications) {
            case 1:
                settings.notificationOn = true;
                break;
            case 2:
                settings.notificationOn = false;
                break;

            default:
                break;
        }
        borrowedItems = new ArrayList<>();

        mNotificationHelper = new NotificationHelper(this);
        if (settings.notificationOn ){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        GMailSender sender = new GMailSender(
                                "techlabapp00@gmail.com",
                                "voordeapp");

//                        sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
                        sender.sendMail("Product geleend", "This email is translated to english below.\nU heeft een product geleend bij TechLab. Deze kunt u op de eerstvolgende werkdag ophalen op de vierde verdieping op het volgende adres:\nWijnhaven 107\n3011 WN Rotterdam\n\nAls u nog vragen heeft, stuur dan een email naar het volgende emailadres: Techlabapp00@gmail.com.\n\nMet vriendelijke groet,\n\nTechLab Team\n\n\nYou have lent a product from TechLab. You can pick it up on the next working day at the fifth floor at the following address: \nWijnhaven 107\n3011 WN Rotterdam\n\nIf you have any questions, you can send an email to the following email address: Techlabapp00@gmail.com.\n\nKind regards,\n\nTechLab Team",
                                "techlabapp00@gmail.com",
                                MenuActivity.personEmail);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                    }
                }
            }).start();
//            SendOnChannel("Return item tomorrow","t2");
//            thread = new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/borrowitems/"), null, 1000, "GET", null);
//                        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
//                        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getBaseContext());
//                        for(JsonElement elem : jsonArray){
//                            JsonObject obj = elem.getAsJsonObject();
////                        Log.d("?????????", "try again");
////                        Log.d("ABCDEFG", obj.get("user").getAsJsonObject().get("email").getAsString());
//                            if (acct.getEmail().equals(obj.get("user").getAsJsonObject().get("email").getAsString())) {
//                                borrowedItems.add(obj);
////                            if (obj.get("item").getAsJsonObject().get("name").getAsString());
//                            }
//                        }
//                    }catch(Exception ex){ ex.printStackTrace();}
//                }
//
//            });
//            // Start the new thread and run the code.
//            thread.start();
//            // Join the thread when it's done, meaning that the application will wait untill the
//            // thread is done.
//            try {
//                thread.join();
//
//            } catch (Exception ex){}
        }
        Log.d("Items", borrowedItems + "");
//        Date currentTime = Calendar.getInstance().getTime();
//        Time now = new Time();
//        now.setToNow();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mmaa");
        String format = sdf.format(Calendar.getInstance().getTime());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date dateBefore1Day = cal.getTime();
        Log.d("!!!!!!!!!!!!!", dateBefore1Day+"");
        setContentView(R.layout.activity_notifications);
        super.onCreateDrawer();
        setTitle(R.string.meldingen);
        notify = (Button)findViewById(R.id.notify);
//        mNotificationHelper = new NotificationHelper(this);
//        notify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SendOnChannel("Return item tomorrow","t2");
//            }
//        });
    }
//    public void SendOnChannel(String title, String description) {
//        NotificationCompat.Builder nb = mNotificationHelper.getCahnnelNotification(title, description);
//        mNotificationHelper.getMAnager().notify(1,nb.build());
//    }

}
