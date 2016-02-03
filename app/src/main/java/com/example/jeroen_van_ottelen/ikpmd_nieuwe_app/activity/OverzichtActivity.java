package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class OverzichtActivity extends Activity
{

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    private List<Subject> cijfers;
    private List<BarEntry> yAs;
    private List<String> xAs;

    private DatabaseReceiver db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht);

//        barChart = (BarChart) findViewById(R.id.barChart);
//        barChart.setDescription("Aantal studiepunten");
//
//        yAs = new ArrayList<>();
//        xAs = new ArrayList<>();
//
//        db = DatabaseReceiver.getDatabaseReceiver(this);
//        cijfers = db.getAllCijfers();
//
//        for(int i = 0; i < cijfers.size(); i++)
//        {
//            yAs.add(new BarEntry(cijfers.get(i).getGrade(), i));
//            xAs.add(cijfers.get(i).getName());
//        }
//        barDataSet = new BarDataSet(yAs, "Aantal studiepunten");
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        barData = new BarData(xAs, barDataSet);
//        barChart.setData(barData);


    }

}
