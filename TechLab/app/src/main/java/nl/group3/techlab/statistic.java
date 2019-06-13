package nl.group3.techlab;
import nl.group3.techlab.AddNewItem;
import nl.group3.techlab.ItemEdit;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import android.content.Intent;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;


public class statistic extends MenuActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    /*int available = AddNewItem.getTotalQuantity();
    int lend = ItemEdit.loanQuantity;
    int totalPro = available + lend;*/
    private TextView lendPer, availabilityPer, brokenint, unbrokenint;
    int intAV;
    int intLE;
    int inttot;
    ArrayList<Book> books;
    String[] arrayStocks;
    int lendsize;
    int size;
    int brokenproducts;
    int unbrokenproducts;


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
        setContentView(R.layout.statistic_layout1);
        super.onCreateDrawer();
        setTitle(R.string.statistieken);

        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intAV = sharedPreferences.getInt("AV", 0);
        intLE = sharedPreferences.getInt("LE", 0);
        inttot = sharedPreferences.getInt("TOT", 0);
        //editor.clear();
        //editor.commit();
        editor.apply();

        /*SharedPreferences prefs = this.getSharedPreferences("nl.group3.techlab", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int intAV = prefs.getInt("AV", 0);
        int intLE = prefs.getInt("LE", 0);
        int inttot = prefs.getInt("TOT", 0);
        editor.apply();
        if (totalPro !=inttot) {
            editor.putInt("AV", intAV+available);
            editor.putInt("LE", intLE+lend);
            editor.putInt("TOT", inttot+totalPro);
            editor.apply();
        }*/

        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {

                    String jsonString = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/borrowitems/", null, 5000, "GET", null);
                    String jsonString2 = JSONHelper.JSONStringFromURL("http://84.86.201.7:8000/api/v1/items/", null, 5000, "GET", null);
                    Log.d("JSON", jsonString);
                    Log.d("JSON", jsonString2);

                    JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
                    JsonArray jsonArray2 = new JsonParser().parse(jsonString2).getAsJsonArray();

                    //arrayStocks = new String[jsonArray2.size()];

                    for (int i=0; i < jsonArray2.size();i++ ){
                        size += jsonArray2.get(i).getAsJsonObject().get("stock").getAsInt();
                        brokenproducts += jsonArray2.get(i).getAsJsonObject().get("broken").getAsInt();
                    }

                    lendsize = jsonArray.size();
                    unbrokenproducts = size + lendsize - brokenproducts;


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

        //final ItemsAndMenuActivity stockTotal = new ItemsAndMenuActivity();
        //stockTotal.loadItems();
        //books = new ArrayList<>();





        lendPer = (TextView) findViewById(R.id.lendPercent);
        availabilityPer = (TextView) findViewById(R.id.availabilityPercent);
        brokenint = (TextView) findViewById(R.id.broken);
        unbrokenint = (TextView) findViewById(R.id.unbroken);

        lendPer.setText(String.valueOf(lendsize));
        availabilityPer.setText(String.valueOf(size));
        brokenint.setText(String.valueOf(brokenproducts));
        unbrokenint.setText(String.valueOf(unbrokenproducts));
        // availabilityPer.setText(String.valueOf(stockTotal.books.size()));

        /*if (inttot>0) {
            int loanFormule = 100/inttot*intLE;
            int availabilityFormule = 100/inttot*intAV;
            lendPer.setText('%' + String.valueOf(loanFormule));
            availabilityPer.setText('%' + String.valueOf(availabilityFormule));
        }*/



        AnimatedPieView Pie = findViewById(R.id.pie);
        AnimatedPieView Pie2 = findViewById(R.id.pie2);

        AnimatedPieViewConfig chart = new AnimatedPieViewConfig();
        //chart.addData(new SimplePieInfo(stockTotal.books.size(), Color.parseColor("#FFFF00"), "A"));
        chart.addData(new SimplePieInfo(size, Color.parseColor("#FFFF00"), "beschikbaar"));
        chart.addData(new SimplePieInfo(lendsize, Color.parseColor("#A80000"), "uitgeleend"));
        chart.duration(1000);
        chart.strokeMode(false);

        AnimatedPieViewConfig chart2 = new AnimatedPieViewConfig();
        chart2.addData(new SimplePieInfo(unbrokenproducts, Color.parseColor("#3333FF"), "onbeschadigd"));
        chart2.addData(new SimplePieInfo(brokenproducts, Color.parseColor("#99FF33"), "beschadigd"));
        chart2.duration(1000);
        chart2.strokeMode(false);

        Pie.applyConfig(chart);
        Pie2.applyConfig(chart2);
        Pie.start();
        Pie2.start();


    }




}
