package dk.gettodone.pro;

import java.util.List;

import dk.gettodone.pro.data.ITasksDataSource;
import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TaskChangedListener;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DoingFragment extends ListFragment {
	private final class TaskFinishedListener implements TaskChangedListener {
		private DoingFragment owner;

		public TaskFinishedListener(DoingFragment owner) {
			this.owner = owner;
		}

		public void onTaskChanged(Task task) {
			owner.refreshListView();
		}
	}
	
	private ITasksDataSource datasource;
	public DoingFragment(ITasksDataSource datasource) {
		this.datasource = datasource;
		datasource.setOnTaskFinishedListener(new TaskFinishedListener(this));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.doing, container, false);
	}
	
	public void refreshListView() {
		ListAdapter adapter = this.getListAdapter();
		if (adapter instanceof ArrayAdapter<?>)
		{
			ArrayAdapter<?> list = (ArrayAdapter<?>)adapter;
			list.notifyDataSetChanged();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task task = (Task) l.getItemAtPosition(position);

		datasource.finishTask(task);
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<Task> tasks = datasource.getContextTasks();

		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(getActivity(),
				android.R.layout.simple_list_item_1, tasks);
		setListAdapter(adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
