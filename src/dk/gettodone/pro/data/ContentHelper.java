package dk.gettodone.pro.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ContentHelper {
	private static String[] taskColumns = new String[] {
			TasksOpenHelper.TABLE_TASKS + "." + TasksOpenHelper.COLUMN_ID,
			TasksOpenHelper.TABLE_TASKS + "."
					+ TasksOpenHelper.COLUMN_TASKS_TITLE,
			TasksOpenHelper.TABLE_TASKS + "."
					+ TasksOpenHelper.COLUMN_TASKS_CONTEXTID,
			TasksOpenHelper.TABLE_TASKS + "."
					+ TasksOpenHelper.COLUMN_TASKS_DELEGATETYPE,
			TasksOpenHelper.TABLE_TASKS + "."
					+ TasksOpenHelper.COLUMN_TASKS_DELEGATEURL };

	public static Task getNextProcessableTask(ContentResolver contentResolver) {
		Cursor cursor = contentResolver.query(
				GetToDoneProContentProvider.TASKS_URI, taskColumns,
				TasksOpenHelper.COLUMN_TASKS_CONTEXTID + " IS NULL AND "
						+ TasksOpenHelper.COLUMN_TASKS_FINISHED
						+ " IS NULL AND "
						+ TasksOpenHelper.COLUMN_TASKS_ISPROJECT
						+ " IS NULL AND "
						+ TasksOpenHelper.COLUMN_TASKS_DELEGATETYPE
						+ " IS NULL", null, TasksOpenHelper.COLUMN_TASKS_TITLE);

		cursor.moveToFirst();
		Task task = null;
		if (!cursor.isAfterLast()) {
			task = Task.fromCursor(cursor);
		}
		cursor.close();
		return task;
	}

	public static void processTaskToProject(ContentResolver contentResolver,
			long id, String title) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_ISPROJECT, 1);

		contentResolver.update(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(id)), mUpdateValues, null, null);

		createTask(contentResolver, title);
	}

	public static long createTask(ContentResolver contentResolver, String title) {
		ContentValues mCreateValues = new ContentValues();

		mCreateValues.put(TasksOpenHelper.COLUMN_TASKS_TITLE, title);

		Uri result = contentResolver.insert(
				GetToDoneProContentProvider.TASKS_URI, mCreateValues);

		return Long.parseLong(result.getLastPathSegment());
	}

	public static void processTaskToContext(ContentResolver contentResolver,
			long taskId, long contextId) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_CONTEXTID, contextId);

		contentResolver.update(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(taskId)), mUpdateValues, null, null);
	}

	public static void processTaskToNewContext(ContentResolver contentResolver,
			long taskId, String contextName) {
		processTaskToContext(contentResolver, taskId,
				createContext(contentResolver, contextName));
	}

	public static long createContext(ContentResolver contentResolver,
			String contextName) {
		ContentValues mCreateValues = new ContentValues();

		mCreateValues.put(TasksOpenHelper.COLUMN_CONTEXTS_NAME, contextName);

		Uri result = contentResolver.insert(
				GetToDoneProContentProvider.CONTEXTS_URI, mCreateValues);

		return Long.parseLong(result.getLastPathSegment());
	}

	public static void deleteTask(ContentResolver contentResolver, long id) {
		contentResolver.delete(GetToDoneProContentProvider.TASKS_URI,
				TasksOpenHelper.COLUMN_ID + " = ?",
				new String[] { Long.toString(id) });
	}

	public static List<Context> getContexts(ContentResolver contentResolver) {
		Cursor contexts = contentResolver.query(
				GetToDoneProContentProvider.CONTEXTS_URI, new String[] {
						TasksOpenHelper.COLUMN_ID,
						TasksOpenHelper.COLUMN_CONTEXTS_NAME }, null, null,
				TasksOpenHelper.COLUMN_CONTEXTS_NAME);

		ArrayList<Context> result = new ArrayList<Context>();

		contexts.moveToFirst();
		while (!contexts.isAfterLast()) {
			result.add(new Context(contexts.getLong(0), contexts.getString(1)));
			contexts.moveToNext();
		}

		return result;
	}

	public static void processTaskToSomeday(ContentResolver contentResolver,
			long id) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_ISPROJECT, 2);

		contentResolver.update(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(id)), mUpdateValues, null, null);
	}

	public static void processTaskToDone(ContentResolver contentResolver,
			long id, String title) {
		finishTask(contentResolver, id);

		createTask(contentResolver, title);
	}

	public static void finishTask(ContentResolver contentResolver, long id) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_FINISHED,
				System.currentTimeMillis() / 1000);

		contentResolver.update(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(id)), mUpdateValues, null, null);
	}

	public static Task getTask(ContentResolver contentResolver, long id) {
		Cursor cursor = contentResolver.query(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(id)), taskColumns, null, null,
				TasksOpenHelper.COLUMN_TASKS_TITLE);

		cursor.moveToFirst();
		Task task = null;
		if (!cursor.isAfterLast()) {
			task = Task.fromCursor(cursor);
		}
		cursor.close();
		return task;
	}

	public static void processTaskToDelegate(ContentResolver contentResolver,
			long id, int type, String uri) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_DELEGATETYPE, type);
		mUpdateValues.put(TasksOpenHelper.COLUMN_TASKS_DELEGATEURL, uri);

		contentResolver.update(
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI, "/"
						+ Long.toString(id)), mUpdateValues, null, null);
	}
}
