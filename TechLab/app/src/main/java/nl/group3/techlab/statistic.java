package nl.group3.techlab;
import nl.group3.techlab.AddNewItem;
import nl.group3.techlab.ItemEdit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;


public class statistic extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    /*int available = AddNewItem.getTotalQuantity();
    int lend = ItemEdit.loanQuantity;
    int totalPro = available + lend;*/
    private TextView lendPer, availabilityPer;
    int intAV;
    int intLE;
    int inttot;


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





        lendPer = (TextView) findViewById(R.id.lendPercent);
        availabilityPer = (TextView) findViewById(R.id.availabilityPercent);
        lendPer.setText(String.valueOf(intLE));
        availabilityPer.setText(String.valueOf(intAV));

        /*if (inttot>0) {
            int loanFormule = 100/inttot*intLE;
            int availabilityFormule = 100/inttot*intAV;
            lendPer.setText('%' + String.valueOf(loanFormule));
            availabilityPer.setText('%' + String.valueOf(availabilityFormule));
        }*/



        AnimatedPieView Pie = findViewById(R.id.pie);
        AnimatedPieViewConfig chart = new AnimatedPieViewConfig();
        chart.addData(new SimplePieInfo(intAV, Color.parseColor("#FFFF00"), "A"));
        chart.addData(new SimplePieInfo(intLE, Color.parseColor("#A80000"), "B"));
        chart.duration(1000);
        chart.strokeMode(false);

        Pie.applyConfig(chart);
        Pie.start();


    }




}
