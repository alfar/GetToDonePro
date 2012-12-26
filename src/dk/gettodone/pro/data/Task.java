package dk.gettodone.pro.data;

import android.database.Cursor;

public class Task {
	private long id;
	private String title;
	private long contextId = 0;
	private long delegateType = 0;
	private String delegateUrl;
	
	public long getId() { return id; }
	public void setId(long value) { id = value; }
	
	public String getTitle() { return title; }
	public void setTitle(String value) { title = value; }
	
	public long getContextId() { return contextId; }
	public void setContextId(long value) { contextId = value; }
	
	public long getDelegateType() { return delegateType; }
	public void setDelegateType(long value) { delegateType = value; }
	
	public String getDelegateUrl() { return delegateUrl; }
	public void setDelegateUrl(String delegateUrl) { this.delegateUrl = delegateUrl; }	
	
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
		
		if (!cursor.isNull(3)) {
			task.setDelegateType(cursor.getLong(3));
		}
		
		if (!cursor.isNull(4)) {
			task.setDelegateUrl(cursor.getString(4));
		}
		
		return task;
	}
}
