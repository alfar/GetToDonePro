package dk.gettodone.pro.data;

public interface ITasksDataSource {
	void pause();

	void resume();

	Task createTask(String title);

	Task getNextProcessableTask();

	void processTaskToContext(Task task, long contextId);

	void deleteTask(Task task);

	void setOnTaskCreatedListener(TaskChangedListener listener);

	void setOnTaskProcessedListener(TaskChangedListener listener);

	void setOnTaskDeletedListener(TaskChangedListener listener);
}
