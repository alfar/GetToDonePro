package dk.gettodone.pro.data;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GetToDoneProContentProvider extends ContentProvider {
	private static final String AUTHORITY = "dk.gettodone.pro.data.GetToDoneProContentProvider";
	private static final String TASKS_BASE_PATH = "tasks";
	private static final String CONTEXTS_BASE_PATH = "contexts";
	private static final int TASKS = 100;
	private static final int TASKS_BYCONTEXT = 102;
	private static final int TASKS_BYCONTEXT_ID = 103;
	private static final int TASK_ID = 110;
	private static final int CONTEXTS = 200;
	private static final int CONTEXT_ID = 210;
	public static final Uri TASKS_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TASKS_BASE_PATH);
	public static final Uri CONTEXTS_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + CONTEXTS_BASE_PATH);
	public static final String CONTENT_TASK_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/task";
	public static final String CONTENT_CONTEXT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/context";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, TASKS_BASE_PATH, TASKS);
		sURIMatcher.addURI(AUTHORITY, TASKS_BASE_PATH + "/context",
				TASKS_BYCONTEXT);
		sURIMatcher.addURI(AUTHORITY, TASKS_BASE_PATH + "/context/#",
				TASKS_BYCONTEXT_ID);
		sURIMatcher.addURI(AUTHORITY, TASKS_BASE_PATH + "/#", TASK_ID);
		sURIMatcher.addURI(AUTHORITY, CONTEXTS_BASE_PATH, CONTEXTS);
		sURIMatcher.addURI(AUTHORITY, CONTEXTS_BASE_PATH + "/#", CONTEXT_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case TASKS:
			rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_TASKS, selection,
					selectionArgs);
			break;
		case TASK_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB
						.delete(TasksOpenHelper.TABLE_TASKS,
								TasksOpenHelper.COLUMN_ID + " = ?",
								new String[] { id });
			} else {
				String[] args = Arrays.copyOf(selectionArgs,
						selectionArgs.length + 1);
				args[selectionArgs.length] = id;
				rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_TASKS,
						selection + " and " + TasksOpenHelper.COLUMN_ID
								+ " = ?", args);
			}
			break;
		case CONTEXTS:
			rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_CONTEXTS,
					selection, selectionArgs);
			break;
		case CONTEXT_ID:
			String cid = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_CONTEXTS,
						TasksOpenHelper.COLUMN_ID + " = ?",
						new String[] { cid });
			} else {
				String[] args = Arrays.copyOf(selectionArgs,
						selectionArgs.length + 1);
				args[selectionArgs.length] = cid;
				rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_CONTEXTS,
						selection + " and " + TasksOpenHelper.COLUMN_ID
								+ " = ?", args);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASKS:
		case TASK_ID:
			return CONTENT_TASK_TYPE;
		case CONTEXTS:
		case CONTEXT_ID:
			return CONTENT_CONTEXT_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long insertedid = 0;
		switch (uriType) {
		case TASKS:
			insertedid = sqlDB
					.insert(TasksOpenHelper.TABLE_TASKS, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse("content://" + AUTHORITY + "/" + TASKS_BASE_PATH
					+ "/" + Long.toString(insertedid));
		case CONTEXTS:
			insertedid = sqlDB.insert(TasksOpenHelper.TABLE_CONTEXTS, null,
					values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse("content://" + AUTHORITY + "/"
					+ CONTEXTS_BASE_PATH + "/" + Long.toString(insertedid));
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
	}

	private TasksOpenHelper database;

	@Override
	public boolean onCreate() {
		database = new TasksOpenHelper(getContext());
		return true;
	}

	private void setupTasksQuery(SQLiteQueryBuilder builder) {
		builder.setTables(TasksOpenHelper.TABLE_TASKS + " left join "
				+ TasksOpenHelper.TABLE_CONTEXTS + " on "
				+ TasksOpenHelper.TABLE_CONTEXTS + "."
				+ TasksOpenHelper.COLUMN_ID + " = "
				+ TasksOpenHelper.TABLE_TASKS + "."
				+ TasksOpenHelper.COLUMN_TASKS_CONTEXTID);
	}

	private void setupContextsQuery(SQLiteQueryBuilder builder) {
		builder.setTables(TasksOpenHelper.TABLE_CONTEXTS);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		int uriType = sURIMatcher.match(uri);

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (uriType) {
		case TASKS:
			setupTasksQuery(queryBuilder);
			break;
		case TASKS_BYCONTEXT:
			setupTasksQuery(queryBuilder);
			queryBuilder.appendWhere(TasksOpenHelper.COLUMN_TASKS_CONTEXTID
					+ " IS NOT NULL AND "
					+ TasksOpenHelper.COLUMN_TASKS_FINISHED + " IS NULL");
			break;
		case TASKS_BYCONTEXT_ID:
			setupTasksQuery(queryBuilder);
			queryBuilder.appendWhere(TasksOpenHelper.COLUMN_TASKS_CONTEXTID
					+ "=" + uri.getLastPathSegment() + " AND "
					+ TasksOpenHelper.COLUMN_TASKS_FINISHED + " IS NULL");
			break;
		case TASK_ID:
			setupTasksQuery(queryBuilder);
			queryBuilder.appendWhere(TasksOpenHelper.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		case CONTEXTS:
			setupContextsQuery(queryBuilder);
			break;
		case CONTEXT_ID:
			setupContextsQuery(queryBuilder);
			queryBuilder.appendWhere(TasksOpenHelper.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		Cursor cursor = queryBuilder.query(database.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case TASKS:
			rowsAffected = sqlDB.delete(TasksOpenHelper.TABLE_TASKS, selection,
					selectionArgs);
			break;
		case TASK_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.update(TasksOpenHelper.TABLE_TASKS,
						values, TasksOpenHelper.COLUMN_ID + " = ?",
						new String[] { id });
			} else {
				String[] args = Arrays.copyOf(selectionArgs,
						selectionArgs.length + 1);
				args[selectionArgs.length] = id;
				rowsAffected = sqlDB.update(TasksOpenHelper.TABLE_TASKS,
						values, selection + " and " + TasksOpenHelper.COLUMN_ID
								+ " = ?", args);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		getContext().getContentResolver().notifyChange(TASKS_URI, null);
		return rowsAffected;
	}

}
