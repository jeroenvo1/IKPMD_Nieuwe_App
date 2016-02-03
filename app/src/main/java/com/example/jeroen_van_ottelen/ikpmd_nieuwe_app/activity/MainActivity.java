package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

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
import android.widget.EditText;
import android.widget.TextView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.InvoerActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;

public class MainActivity extends ActionBarActivity
{
    private SharedPreferences SP;
    private SharedPreferences.Editor editor;

    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PreferenceManager en editor aanmaken.
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = SP.edit();

        // Textview user_name maken
        user_name = (TextView) findViewById(R.id.naam_user);

        // Als er nog nooi een naam is ingevoerd, voer die dan in
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
    }

    public void showCijfers(View view)
    {
        System.out.println("HALLLLLLLLLOOOOOOO");
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
        }

        return super.onOptionsItemSelected(item);
    }
}
