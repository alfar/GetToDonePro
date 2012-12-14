package dk.gettodone.pro.data;

public interface ITasksDataSource {
	void pause();
	void resume();
	
	Task createTask(String title);
	void deleteTask(Task task);
	void setOnTaskCreatedListener(TaskChangedListener listener);
	void setOnTaskDeletedListener(TaskChangedListener listener);
}
