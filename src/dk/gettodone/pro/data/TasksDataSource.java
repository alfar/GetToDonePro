package dk.gettodone.pro.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TasksDataSource implements ITasksDataSource {
	private SQLiteDatabase database;
	private TasksOpenHelper dbhelper;
	private String[] allColumns = { TasksOpenHelper.COLUMN_ID, TasksOpenHelper.COLUMN_TASKS_TITLE };

	private ArrayList<TaskChangedListener> createdListeners = new ArrayList<TaskChangedListener>();
	private ArrayList<TaskChangedListener> deletedListeners = new ArrayList<TaskChangedListener>();

	public TasksDataSource(Context context) {
		dbhelper = new TasksOpenHelper(context);		
	}

	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}

	public Task createTask(String title) {
		ContentValues values = new ContentValues();
		values.put(TasksOpenHelper.COLUMN_TASKS_TITLE, title);
		long insertId = database.insert(TasksOpenHelper.TABLE_TASKS, null, values);
		Cursor cursor = database.query(TasksOpenHelper.TABLE_TASKS, allColumns, TasksOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();
		raiseOnTaskChanged(createdListeners, newTask);
		return newTask;
	}

	public List<Task> getAllTasks() {
		List<Task> tasks = new ArrayList<Task>();

		Cursor cursor = database.query(TasksOpenHelper.TABLE_TASKS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return tasks;
	}

	public Task getNextProcessableTask() {
		Cursor cursor = database.query(TasksOpenHelper.TABLE_TASKS,
				allColumns, null, null, null, null, TasksOpenHelper.COLUMN_ID + " desc", "1");

		cursor.moveToFirst();
		Task task = null;
		if (!cursor.isAfterLast()) {
			task= cursorToTask(cursor);
		}
		cursor.close();
		return task;
	}

	public void deleteTask(Task task) {
		long id = task.getId();
		database.delete(TasksOpenHelper.TABLE_TASKS, TasksOpenHelper.COLUMN_ID + " = " + id, null);
		raiseOnTaskChanged(deletedListeners, task);
	}

	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTitle(cursor.getString(1));
		return task;
	}

	public void pause() {
		this.close();
	}

	public void resume() {
		this.open();		
	}

	public void setOnTaskCreatedListener(TaskChangedListener listener) {
		createdListeners.add(listener);
	}

	public void setOnTaskDeletedListener(TaskChangedListener listener) {
		deletedListeners.add(listener);
	}

	private void raiseOnTaskChanged(ArrayList<TaskChangedListener> listeners, Task task) {
		for (TaskChangedListener listener : listeners) {
			listener.onTaskChanged(task);
		}
	}
}
