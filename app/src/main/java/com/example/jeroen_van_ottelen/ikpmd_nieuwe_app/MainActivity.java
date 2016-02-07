package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity.InvoerActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity.OverzichtActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.gson.GsonRequest;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.volley.VolleyHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity
{

    private SharedPreferences SP;
    private SharedPreferences.Editor editor;
    private DatabaseReceiver db;

    private TextView user_name;
    private TextView studiepuntenText;
    private int studiepunten;

    private ListView recentIngevoerd;
    private ArrayAdapter adapter;
    private List<String> recentIngevoerdList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestSubjects();
        setContentView(R.layout.activity_main);

        db = DatabaseReceiver.getDatabaseReceiver(this);

        // PreferenceManager en editor aanmaken.
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = SP.edit();

        studiepunten = db.getCurrentEcts();

        // Textview user_name en studiepuntenText maken
        user_name = (TextView) findViewById(R.id.naam_user);
        studiepuntenText = (TextView) findViewById(R.id.studiepuntenText);
        studiepuntenText.setText("Totaal aantal studiepunten: " + studiepunten);


        // Als er nog nooit een naam is ingevoerd, voer die dan in
        if(SP.getString("username", null) == null)
        {
            // Maak een allert scherm en zet de naam
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Voer uw naam in");

            // Input maken
            final EditText input = new EditText(this);

            // Inupt soort instellen, alleen text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Buttons en onClickListemer maken
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Als er op de button geklikt wordt, zet de ingevoerde naam (username) in de
                    // preferences en commit hem.
                    editor.putString("username", input.getText().toString());
                    editor.commit();

                    // Haal de naam uit de preference, en zet hem in het textvak
                    user_name.setText(SP.getString("username", null));
                }
            });

            builder.show();
        }
        // Als er ooit een keer een naam is ingevuld, haal deze uit de preferences en zet
        // het in het textvak.
        else
        {
            user_name.setText(SP.getString("username", null));
        }

        recentIngevoerd = (ListView) findViewById(R.id.recent_ingevoerd_list);

        recentIngevoerdList = new ArrayList<>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recentIngevoerdList);
        recentIngevoerd.setAdapter(adapter);
    }

    public void showCijfers(View view)
    {

        // Maak een allert scherm en zet de naam
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Studiepunten");

        // Input maken
        final TextView text1 = new TextView(this);

        if(studiepunten == 60)
        {
            text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
                    "Propedeuse gehaald.");
        }
        else if(studiepunten >= 50 && studiepunten < 60)
        {
            text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
                    "Voldoende studiepunten gehaald om naar het tweede jaar te gaan, maar ropedeuse nog niet gehaald.");
        }
        else if(studiepunten >= 40 && studiepunten < 50)
        {
            text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
                    "Voldoende studiepunten gehaald om geen negatief BSA te krijgen, maar nog niet genoeg om naar het tweede jaar te gaan.");
        } else
        {
            text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
                    "Helaas niet voldoende studiepunten gehaald, je krijgt een negatief BSA");
        }

        builder.setView(text1);

        // Buttons en onClickListemer maken
        builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });

        // Buttons en onClickListemer maken
        builder.setPositiveButton("Overzicht", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(MainActivity.this, OverzichtActivity.class));
            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_invoeren)
        {
            startActivity(new Intent(this, InvoerActivity.class));
        } else if(id == R.id.menu_overzicht)
        {
            startActivity(new Intent(this, OverzichtActivity.class));
        } else if(id == R.id.reset_preferences)
        {
            editor.clear();
            editor.commit();

            db.resetDB();
        }

        return super.onOptionsItemSelected(item);
    }

    // Get all the subjects
    private void requestSubjects(){
        Type type = new TypeToken<List<Subject>>(){}.getType();

        GsonRequest<List<Subject>> request = new GsonRequest<>("http://www.fuujokan.nl/subject_lijst.json",
                type, null, new Response.Listener<List<Subject>>() {
            @Override
            public void onResponse(List<Subject> response) {
                processRequestSucces(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                processRequestError(error);
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }

    // When the subjects are with succes received, put them into the database
    private void processRequestSucces(List<Subject> subjects ){

        // putting all received classes in my database.
        DatabaseReceiver databaseReceiver = DatabaseReceiver.getDatabaseReceiver(this);

        for (Subject subject : subjects) {
            subject.setGrade(0);
            databaseReceiver.insertSubject(subject);
        }
    }

    private void processRequestError(VolleyError error){
        // WAT ZULLEN WE HIERMEE DOEN ?? - niets..
    }

}
