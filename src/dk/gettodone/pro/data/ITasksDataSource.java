package dk.gettodone.pro.data;

import java.util.List;

public interface ITasksDataSource {
	void pause();

	void resume();

	Task createTask(String title);

	Task getNextProcessableTask();
	
	List<Task> getContextTasks();

	void processTaskToContext(Task task, long contextId);
	
	void finishTask(Task task);

	void deleteTask(Task task);

	void setOnTaskCreatedListener(TaskChangedListener listener);

	void setOnTaskProcessedListener(TaskChangedListener listener);

	void setOnTaskDeletedListener(TaskChangedListener listener);

	void setOnTaskFinishedListener(TaskChangedListener listener);
}
