package dk.gettodone.pro.data;

import android.database.Cursor;

public class Task {
	private long id;
	private String title;
	private long contextId = 0;
	
	public long getId() { return id; }
	public void setId(long value) { id = value; }
	
	public String getTitle() { return title; }
	public void setTitle(String value) { title = value; }
	
	public long getContextId() { return contextId; }
	public void setContextId(long value) { contextId = value; }
	
	@Override
	public String toString() {
		return title;
	}
	
	public static Task fromCursor(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTitle(cursor.getString(1));
		if (!cursor.isNull(2)) {
			task.setContextId(cursor.getLong(2));
		}
		return task;	
	}
}
