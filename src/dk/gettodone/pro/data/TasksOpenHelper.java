package dk.gettodone.pro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "gettodone.db";
	public static final String TABLE_TASKS = "Tasks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TASKS_TITLE = "title";
	public static final String COLUMN_TASKS_CONTEXTID = "contextId";
	public static final String COLUMN_TASKS_FINISHED = "finished";
	public static final String TABLE_CONTEXTS = "Contexts";
	public static final String COLUMN_CONTEXTS_NAME = "name";

	private static final String TASKS_TABLE_CREATE = "create table " + TABLE_TASKS + " (" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TASKS_TITLE + " text not null, " +
			COLUMN_TASKS_CONTEXTID + " integer null, " + 
			COLUMN_TASKS_FINISHED + " datetime null)";

	private static final String TASKS_TABLE_ADDCONTEXTS = "alter table " + TABLE_TASKS + " add column " +
			COLUMN_TASKS_CONTEXTID + " integer null";

	private static final String TASKS_TABLE_ADDFINISHED = "alter table " + TABLE_TASKS + " add column " +
			COLUMN_TASKS_FINISHED + " datetime null";

	private static final String CONTEXTS_TABLE_CREATE = "create table " + TABLE_CONTEXTS + " (" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_CONTEXTS_NAME + " text not null)";
	
	public TasksOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TASKS_TABLE_CREATE);
		db.execSQL(CONTEXTS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			db.execSQL(TASKS_TABLE_ADDCONTEXTS);
			db.execSQL(CONTEXTS_TABLE_CREATE);			
		}
		if (oldVersion < 3) {
			db.execSQL(TASKS_TABLE_ADDFINISHED);
		}
	}

}
