package com.example.statistieken;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimatedPieView Pie = findViewById(R.id.pie);
        AnimatedPieViewConfig chart = new AnimatedPieViewConfig();
        chart.addData(new SimplePieInfo(2500, Color.parseColor("#A80000"), "A"));
        chart.addData(new SimplePieInfo(2500, Color.parseColor("#FFFF00"), "B"));
        chart.duration(1000);
        chart.strokeMode(false);

        Pie.applyConfig(chart);
        Pie.start();


    }
}
