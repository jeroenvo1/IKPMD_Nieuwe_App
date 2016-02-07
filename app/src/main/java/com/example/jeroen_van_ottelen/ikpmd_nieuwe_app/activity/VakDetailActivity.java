package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database.DatabaseReceiver;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

/**
 * @author Richard Jongenburger
 * Activity with subject details.
 */

public class VakDetailActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vak_detail);

		final String subjectName = getIntent().getStringExtra("subjectName");
		final DatabaseReceiver databaseReceiver = DatabaseReceiver.getDatabaseReceiver(this);

		final Subject subject = databaseReceiver.getSubject(subjectName);

		// Set all the texts of the variables on the screen.
		final TextView nameView = (TextView) findViewById(R.id.subjectName);
		nameView.setText(subject.getName());

		final TextView ectsView = (TextView) findViewById(R.id.subjectEcts);
		String ects = subject.getEcts() + "";
		ectsView.setText(ects);

		final TextView periodView = (TextView) findViewById(R.id.subjectPeriod);
		String period = subject.getPeriod() + "";
		periodView.setText(period);

		final TextView gradeView = (EditText) findViewById(R.id.grade);

		if(subject.getGrade() == 0.0f)
		{
			gradeView.setText("");
		}
		else
		{
			if(subject.getGrade() > 5.5)
			{
				gradeView.setTextColor(getResources().getColor(R.color.dark_green));
			}
			else
			{
				gradeView.setTextColor(Color.RED);
			}
			String grade = subject.getGrade() + "";
			gradeView.setText(grade);
		}

		// Set color to black again when the text change.
		gradeView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				gradeView.setTextColor(Color.BLACK);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		// Go back to previous activity when u press on cancel
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		final Button saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				subject.setEcts(Integer.parseInt(ectsView.getText().toString()));
				subject.setPeriod(Integer.parseInt(periodView.getText().toString()));

				String grade = gradeView.getText().toString();

				// Return to previous activity if there is nothing filled in.
				if (grade.isEmpty()) {
					finish();
					return;
				} else {
					subject.setGrade(Float.parseFloat(grade));
				}

				if (validateGrade(subject.getGrade(), gradeView)) {
					databaseReceiver.updateSubject(subject);
					finish();
				}
			}
		});
	}

	public boolean validateGrade(float grade, TextView editText)
	{
		if(grade < 1.0 || grade > 10.0)
		{
			editText.setError("Cijfer moet tussen 1.0 en 10.0 zijn");
			return false;
		}

		if(grade == 10.0 || ((grade + "").length() == 3 && (grade + "").charAt(1) == '.'))
		{
			return true;
		}
		else {
			editText.setError("Cijfer moet '1.0' formaat hebben");
			return false;
		}
	}
}