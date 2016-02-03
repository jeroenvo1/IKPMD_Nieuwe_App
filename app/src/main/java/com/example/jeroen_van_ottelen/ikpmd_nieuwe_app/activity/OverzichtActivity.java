package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverzichtActivity extends ActionBarActivity
{

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    private List<Subject> cijfers;
    private List<BarEntry> yAs;
    private List<String> xAs;
    private int[] colors;

    private ExpandableListView expListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private boolean period;
    private int selectedPeriod = 10;

    private DatabaseReceiver db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht);

        // Barchart maken
        barChart = (BarChart) findViewById(R.id.barChart);

        // DB object maken
        db = DatabaseReceiver.getDatabaseReceiver(this);

        // Default barchart tonen
        setBarByPeriode(new View(this));

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (period) {
                    prepareListData(db.getSubjectsByPeriod(e.getXIndex()));
                    selectedPeriod = e.getXIndex();
                }
            }

            @Override
            public void onNothingSelected() {
                // 10 is een random waarde waardoor de db een lege lijst terug geeft
                prepareListData(db.getSubjectsByPeriod(10));
                selectedPeriod = 10;
            }
        });

    }

    // Barchart waar alle cijfers van alle vakken in staan
    public void setBarByGrade(View view)
    {
        period = false;

        // Naam in de tabel zetten
        barChart.setDescription("Vak");

        // Alle cijfers die hoger dan 0 zijn ophalen
        if(selectedPeriod != 10) {
            cijfers = db.getSubjectsByPeriod(selectedPeriod);
        } else {
            cijfers = db.getAllSubjects();
        }

        // Een ArrayList van de x en y as maken
        yAs = new ArrayList<>();
        xAs = new ArrayList<>();

        // Een array van ints maken om de charts verschillende kleuren te kunnen geven
        colors = new int[cijfers.size()];

        // for loop door alle vakken
        for(int i = 0; i < cijfers.size(); i++)
        {
            // Een nieuw BarEnrty voor elk vak maken
            yAs.add(new BarEntry(cijfers.get(i).getGrade(), i));

            // Naam van het vak aan de x as voegen
            xAs.add(cijfers.get(i).getName());

            // Als het cijfer een 7 of hoger is, dan wordt de bar groen.
            // Is het cijfer tussen een 5.5 en 7, dan wordt de bar oranje.
            // Is het cijfer een 5.5 of lager, dan wordt de bar rood.
            if(cijfers.get(i).getGrade() >= 7)
            {
                colors[i] = colors[i] = Color.rgb(106, 150, 31);
            } else if(cijfers.get(i).getGrade() >= 5.5 && cijfers.get(i).getGrade() < 7)
            {
                colors[i] = Color.rgb(255, 102, 0);
            } else
            {
                colors[i] = Color.rgb(193, 37, 82);
            }

        }

        // Data van voor de BarChart adden
        barDataSet = new BarDataSet(yAs, "Aantal studiepunten");
        barDataSet.setColors(colors);
        barData = new BarData(xAs, barDataSet);
        barChart.setData(barData);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    public void setBarByPeriode(View view)
    {
        period = true;

        barChart.setDescription("Periode");

        yAs = db.getEctsByPeriod();
        xAs = new ArrayList<>();

        colors = new int[db.getEctsByPeriod().size()];

        // Een array van ints maken om de charts verschillende kleuren te kunnen geven
        int[] maxEcts = db.getMaxEctsPerPeriod();

        for(int i = 0; i < yAs.size(); i++)
        {
            xAs.add(Integer.toString(i + 1));
            if(yAs.get(i).getVal() >  maxEcts[i]*0.7)
            {
                colors[i] = Color.rgb(106, 150, 31);
            } else if(yAs.get(i).getVal() > maxEcts[i]*0.55 && yAs.get(i).getVal() < maxEcts[i]*0.7)
            {
                colors[i] = Color.rgb(255, 102, 0);
            } else
            {
                colors[i] = Color.rgb(193, 37, 82);
            }

        }

        barDataSet = new BarDataSet(yAs, "Aantal studiepunten");
        barDataSet.setColors(colors);
        barData = new BarData(xAs, barDataSet);
        barChart.setData(barData);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    private void prepareListData(List<Subject> subjects)
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        int i = 0;
        for(Subject subject: subjects)
        {
            expListView = (ExpandableListView) findViewById(R.id.listView);
            listDataHeader.add(subject.getName());

            List<String> list = new ArrayList<>();
            list.add("Aantal studiepunten: " + subject.getEcts());
            list.add("Periode: " + subject.getPeriod());
            list.add("Cijfer: " + subject.getGrade());
            listDataChild.put(listDataHeader.get(i), list);
            i++;
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, subjects);
        expListView.setAdapter(listAdapter);
    }
}
