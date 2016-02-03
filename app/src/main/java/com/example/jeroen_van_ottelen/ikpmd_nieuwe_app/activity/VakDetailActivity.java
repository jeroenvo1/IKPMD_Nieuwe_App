package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;

public class VakDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vak_detail);

		String subjectName = getIntent().getStringExtra("subjectName");
	}

}
