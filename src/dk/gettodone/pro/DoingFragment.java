package dk.gettodone.pro;

import java.util.List;

import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TasksDataSource;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DoingFragment extends ListFragment {
	private TasksDataSource datasource;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.doing, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task task = (Task) l.getItemAtPosition(position);

		datasource.deleteTask(task);
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		datasource = new TasksDataSource(getActivity());
		datasource.open();

		List<Task> tasks = datasource.getAllTasks();

		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(getActivity(),
				android.R.layout.simple_list_item_1, tasks);
		setListAdapter(adapter);
	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}

}
