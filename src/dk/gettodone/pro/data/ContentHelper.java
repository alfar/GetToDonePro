package dk.gettodone.pro.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

public class ContentHelper {
	public static void processTaskToContext(ContentResolver contentResolver, long taskId, long contextId) {
		ContentValues mUpdateValues = new ContentValues();

		mUpdateValues.put(
				TasksOpenHelper.COLUMN_TASKS_CONTEXTID, contextId);

		contentResolver.update(
				Uri.withAppendedPath(
						GetToDoneProContentProvider.TASKS_URI,
						"/" + Long.toString(taskId)),
				mUpdateValues, null, null);		
	}
	
	public static void processTaskToNewContext(ContentResolver contentResolver, long taskId, String contextName) {
		processTaskToContext(contentResolver, taskId, createContext(contentResolver, contextName));
	}
	
	public static long createContext(ContentResolver contentResolver, String contextName) {
		ContentValues mCreateValues = new ContentValues();

		mCreateValues.put(
				TasksOpenHelper.COLUMN_CONTEXTS_NAME, contextName);
		
		Uri result = contentResolver.insert(GetToDoneProContentProvider.CONTEXTS_URI, mCreateValues);
		
		return Long.parseLong(result.getLastPathSegment());
	}
}
