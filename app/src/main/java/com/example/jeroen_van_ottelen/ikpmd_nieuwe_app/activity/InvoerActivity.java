package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard Jongenburger
 * This activity represents the screen where u can pick a subject that are in the database.
 */

public class InvoerActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoer);

		DatabaseReceiver databaseReceiver = DatabaseReceiver.getDatabaseReceiver(this);
		final List<Subject> subjects = databaseReceiver.getAllSubjects();

		ListView subjectList = (ListView) findViewById(R.id.subjectList);

		subjectList.setClickable(true);
		subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3)
			{
				Subject selectedSubject = subjects.get(position);
				Intent intent = new Intent(view.getContext(), VakDetailActivity.class);

				// Give the name of the clicked subject to VakDetailActivity.
				intent.putExtra("subjectName", selectedSubject.getName());
				startActivity(intent);
			}
		});

		// We have to give string of subject names to adapter.
		ArrayList<String> subjectNames = new ArrayList<>();

		for(Subject subject : subjects)
		{
			subjectNames.add(subject.getName());
		}

		ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectNames);
		subjectList.setAdapter(adapter);
	}
}
