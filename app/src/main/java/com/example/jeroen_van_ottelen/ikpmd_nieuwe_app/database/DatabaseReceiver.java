package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.database;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates and handles everything in the database.
 * It's singleton so that you can only have one instance of this class.
 * @author Richard Jongenburger
 */

public class DatabaseReceiver extends SQLiteOpenHelper {

	public static SQLiteDatabase database;
	private static DatabaseReceiver databaseReceiver;
	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	private Context context;

	private DatabaseReceiver(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public static synchronized DatabaseReceiver getDatabaseReceiver(Context context)
	{
		if(databaseReceiver == null)
		{
			databaseReceiver = new DatabaseReceiver(context);
			database = databaseReceiver.getWritableDatabase();
		}
		return databaseReceiver;
	}

	/**
	 * Create all the tables from the database.
	 * @param db The database in which the tables should be created.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DatabaseInfo.Subjects.TABLE_NAME + " (" +
						DatabaseInfo.Subjects.COLUMN_NAME_NAME + " VARCHAR(100) PRIMARY KEY, " +
						DatabaseInfo.Subjects.COLUMN_NAME_ECTS + " INTEGER, " +
						DatabaseInfo.Subjects.COLUMN_NAME_PERIOD + " INTEGER, " +
						DatabaseInfo.Subjects.COLUMN_NAME_GRADE + " INTEGER );"
		);
	}

	/**
	 * Used to recreate the database when there is a new version.
	 * @param db The database to be recreated.
	 * @param oldVersion The old version of the database.
	 * @param newVersion The new version the database.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.Subjects.TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Insert content values into a table from the database.
	 * @param table The name of the database
	 * @param nullColumnHack optional; may be null. SQL doesn't allow inserting a completely empty row without naming at least one column name. (Developer.android.com)
	 * @param values The values that should be inserted.
	 */
	public void insert(String table, String nullColumnHack, ContentValues values){
		database.insert(table, nullColumnHack, values);
	}

	/**
	 * Do a query in the database.
	 * @param table The table name to compile the query against.
	 * @param columns A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
	 * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
	 * @param selectArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
	 * @param groupBy A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
	 * @param having A filter declare which row groups to include in the cursor, if row grouping is being used,
	 *               formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
	 * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
	 * @return A cursor object which is positioned before the first entry.
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
		return database.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
	}

	public void insertSubject(Subject subject)
	{
		ContentValues cv = new ContentValues();
		cv.put(DatabaseInfo.Subjects.COLUMN_NAME_NAME, subject.getName());
		cv.put(DatabaseInfo.Subjects.COLUMN_NAME_ECTS, subject.getEcts());
		cv.put(DatabaseInfo.Subjects.COLUMN_NAME_PERIOD, subject.getPeriod());
		cv.put(DatabaseInfo.Subjects.COLUMN_NAME_GRADE, subject.getGrade());

		insert(DatabaseInfo.Subjects.TABLE_NAME, null, cv);
	}

	public void deleteSubject(Subject subject)
	{
		String name = subject.getName();
		database.delete(DatabaseInfo.Subjects.TABLE_NAME, DatabaseInfo.Subjects.COLUMN_NAME_NAME + "='" + name + "'", null);
	}

	public Subject getSubject(String name)
	{

		Cursor c = query(DatabaseInfo.Subjects.TABLE_NAME, null, DatabaseInfo.Subjects.COLUMN_NAME_NAME + "='" + name + "'", null, null, null, null);
		c.moveToFirst();

		Subject subject = new Subject();
		subject.setName(c.getString(0));
		subject.setEcts(c.getInt(1));
		subject.setPeriod(c.getInt(2));
		subject.setGrade(c.getInt(3));

		return subject;
	}

	public List<Subject> getAllSubjects()
	{
		List<Subject> subjects = new ArrayList<>();

		Cursor c = query(DatabaseInfo.Subjects.TABLE_NAME, null, null, null, null, null, null);

		while(c.moveToNext())
		{
			Subject subject = new Subject();
			subject.setName(c.getString(0));
			subject.setEcts(c.getInt(1));
			subject.setPeriod(c.getInt(2));
			subject.setGrade(c.getInt(3));

			subjects.add(subject);
		}
		System.out.println(subjects);
		return subjects;
	}

	public List<Subject> getAllCijfers()
	{
		List<Subject> subjects = new ArrayList<>();

		Cursor c = query(DatabaseInfo.Subjects.TABLE_NAME, null, DatabaseInfo.Subjects.COLUMN_NAME_GRADE + ">0", null, null, null, null);

		while(c.moveToNext())
		{
			Subject subject = new Subject();
			subject.setName(c.getString(0));
			subject.setGrade(c.getInt(1));

			subjects.add(subject);
		}

		return subjects;
	}
}
