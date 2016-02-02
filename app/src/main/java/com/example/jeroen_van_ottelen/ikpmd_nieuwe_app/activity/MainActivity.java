package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;

public class MainActivity extends ActionBarActivity
{

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(true)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Voer uw naam in");

            // Input maken
            final EditText input = new EditText(this);

            // Inupt soort instellen, alleen text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    username = input.getText().toString();
                    System.out.println("username: " + username);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });

            builder.show();
        }


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
}
