package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseInfo;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.gson.GsonRequest;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.volley.VolleyHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestSubjects();
        setContentView(R.layout.activity_main);
        DatabaseReceiver.getDatabaseReceiver(this);
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
