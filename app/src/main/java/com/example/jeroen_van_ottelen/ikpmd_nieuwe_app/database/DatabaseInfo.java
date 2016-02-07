package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database;

import android.provider.BaseColumns;
/**
 * @author Richard Jongenburger
 *
 * This class is used to hold all the table names and column names.
 */

public final class DatabaseInfo
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	private DatabaseInfo() {}

	public static abstract class Subjects implements BaseColumns
	{
		public static final String TABLE_NAME = "subject";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_ECTS = "ects";
		public static final String COLUMN_NAME_PERIOD = "period";
		public static final String COLUMN_NAME_GRADE = "grade";
	}
}

