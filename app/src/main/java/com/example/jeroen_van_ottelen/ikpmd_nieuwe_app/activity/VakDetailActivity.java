package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

public class VakDetailActivity extends ActionBarActivity {

	private DatabaseReceiver databaseReceiver;

	private TextView nameView;
	private TextView ectsView;
	private TextView periodView;
	private TextView gradeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vak_detail);

		final String subjectName = getIntent().getStringExtra("subjectName");
		databaseReceiver = databaseReceiver.getDatabaseReceiver(this);

		final Subject subject = databaseReceiver.getSubject(subjectName);

		// Set all the texts of the variables on the screen.
		nameView = (TextView) findViewById(R.id.subjectName);
		nameView.setText(subject.getName());

		ectsView = (TextView) findViewById(R.id.subjectEcts);
		ectsView.setText(subject.getEcts() + "");

		periodView = (TextView) findViewById(R.id.subjectPeriod);
		periodView.setText(subject.getPeriod() + "");

		gradeView = (EditText) findViewById(R.id.grade);

		if(subject.getGrade() == 0.0f) {
			gradeView.setText("");
		}
		else {
			gradeView.setText(subject.getGrade() + "");
		}


		// Go back to previous activity when u press on cancel
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Save the subject and go back to previous activity
		final Button saveBtn = (Button) findViewById(R.id.saveBtn);

		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				subject.setEcts(Integer.parseInt(ectsView.getText().toString()));
				subject.setPeriod(Integer.parseInt(periodView.getText().toString()));

				String grade = gradeView.getText().toString();
				TextView errorView = (TextView) findViewById(R.id.vak_details_error);

				if(grade.isEmpty())
				{
					finish();
					return;
				}
				else
				{
					subject.setGrade(Float.parseFloat(gradeView.getText().toString()));
				}

				if((subject.getGrade() + "").charAt(1) != '.')
				{
					errorView.setText("Cijfer moet '1.0' formaat hebben");
				}
				else if(subject.getGrade() < 1.0f)
				{
					errorView.setText("Cijfer moet minimaal 1.0 zijn");
				}
				else
				{
					databaseReceiver.updateSubject(subject);
					
					finish();
				}
		}});


	}
}

