package dk.gettodone.pro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "gettodone.db";
	public static final String TABLE_TASKS = "Tasks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TASKS_TITLE = "title";
	private static final String TASKS_TABLE_CREATE = "create table " + TABLE_TASKS + " (" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TASKS_TITLE + " text not null)";
	
	public TasksOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TASKS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	}

}