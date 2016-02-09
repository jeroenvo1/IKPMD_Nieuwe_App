package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app;

/**
 *  Jeroen van Ottelen
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.Custom.CustomAdapter;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.Custom.CustomSharedPreferences;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity.InvoerActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity.OverzichtActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity.VakDetailActivity;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.gson.GsonRequest;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.volley.VolleyHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity {

	private CustomSharedPreferences prefs;
	private DatabaseReceiver db;

	private TextView user_name;
	private TextView studiepuntenText;
	private int studiepunten;

	private ListView recentIngevoerd;
	private CustomAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestSubjects();
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle(getSchoolYear());

		db = DatabaseReceiver.getDatabaseReceiver(this);

		// Custom PreferenceManager aanmaken (nodig om objecten te kunnen opslaan
		prefs = new CustomSharedPreferences(this);

		studiepunten = db.getCurrentEcts();

		resumableData();

		// Als er nog nooit een naam is ingevoerd, voer die dan in
		if (prefs.getString("username") == null) {
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
					prefs.putString("username", input.getText().toString());

					// Haal de naam uit de preference, en zet hem in het textvak
					user_name.setText(prefs.getString("username"));
				}
			});

			builder.show();
		}
		// Als er ooit een keer een naam is ingevuld, haal deze uit de preferences en zet
		// het in het textvak.
		else {
			user_name.setText(prefs.getString("username"));
		}

		checkBSA();
	}

	public String getSchoolYear()
	{
		Calendar calendar = Calendar.getInstance();

		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int secondYear = 0;

		// Check if it's before september
		if(month < 9)
		{
			secondYear = year - 1;
		}

		else
		{
			secondYear = year + 1;
		}

		return secondYear + " - " + year;

	}

	public void checkBSA()
	{
		if(db.bsaCheck()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("BSA drijgt");

			// Input maken
			TextView text1 = new TextView(this);
			text1.setText("\nPas op, je hebt de kans op een BSA");

			builder.setView(text1);

			// Buttons en onClickListemer maken
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			builder.show();
		}
	}

	public void resumableData() {
		// Textview user_name en studiepuntenText maken
		user_name = (TextView) findViewById(R.id.naam_user);
		studiepuntenText = (TextView) findViewById(R.id.studiepuntenText);
		studiepuntenText.setText("Totaal aantal studiepunten: " + studiepunten);

		recentIngevoerd = (ListView) findViewById(R.id.recent_ingevoerd_list);
		adapter = new CustomAdapter(this, 0, prefs.getListObject("recentIngevoerd"));
		recentIngevoerd.setAdapter(adapter);

		recentIngevoerd.setClickable(true);
		recentIngevoerd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
				Subject selectedSubject = (Subject) view.getItemAtPosition(position);
				Intent intent = new Intent(view.getContext(), VakDetailActivity.class);

				// Give the name of the clicked subject to VakDetailActivity.
				intent.putExtra("subjectName", selectedSubject.getName());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		resumableData();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		finish();
		startActivity(getIntent());
	}

	public void showCijfers(View view) {

		// Maak een allert scherm en zet de naam
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Studiepunten");

		// Input maken
		final TextView text1 = new TextView(this);

		if (studiepunten == 60) {
			text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
					"Propedeuse gehaald.");
		} else if (studiepunten >= 50 && studiepunten < 60) {
			text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
					"Voldoende studiepunten gehaald om naar het tweede jaar te gaan, maar ropedeuse nog niet gehaald.");
		} else if (studiepunten >= 40 && studiepunten < 50) {
			text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
					"Voldoende studiepunten gehaald om geen negatief BSA te krijgen, maar nog niet genoeg om naar het tweede jaar te gaan.");
		} else {
			text1.setText("Aantal studiepunten: " + studiepunten + "\n" +
					"Helaas niet voldoende studiepunten gehaald, je krijgt een negatief BSA");
		}

		builder.setView(text1);

		// Buttons en onClickListemer maken
		builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Buttons en onClickListemer maken
		builder.setPositiveButton("Overzicht", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(MainActivity.this, OverzichtActivity.class));
			}
		});

		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.menu_invoeren) {
			startActivity(new Intent(this, InvoerActivity.class));
		} else if (id == R.id.menu_overzicht) {
			startActivity(new Intent(this, OverzichtActivity.class));
		} else if (id == R.id.reset_preferences) {
			prefs.clear();
			db.resetDB();
			onRestart();
		}

		return super.onOptionsItemSelected(item);
	}

	// Get all the subjects
	private void requestSubjects() {
		Type type = new TypeToken<List<Subject>>() {
		}.getType();

		GsonRequest<List<Subject>> request = new GsonRequest<>("http://www.fuujokan.nl/subject_lijst.json",
				type, null, new Response.Listener<List<Subject>>() {
			@Override
			public void onResponse(List<Subject> response) {
				processRequestSucces(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				processRequestError(error);
			}
		});
		VolleyHelper.getInstance(this).addToRequestQueue(request);
	}

	// When the subjects are with succes received, put them into the database
	private void processRequestSucces(List<Subject> subjects) {

		// putting all received classes in my database.
		DatabaseReceiver databaseReceiver = DatabaseReceiver.getDatabaseReceiver(this);

		for (Subject subject : subjects) {
			subject.setGrade(0);
			databaseReceiver.insertSubject(subject);
		}
	}

	private void processRequestError(VolleyError error) {
		if (error instanceof NoConnectionError) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Geen internet beschikbaar");

			// Buttons en onClickListemer maken
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			builder.show();
		}

	}
}