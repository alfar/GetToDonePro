package dk.gettodone.pro;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import dk.gettodone.pro.data.*;

public class GetToDoneProDoingActivity extends ListActivity {
	private TasksDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.doing);

		datasource = new TasksDataSource(this);
		datasource.open();

		List<Task> tasks = datasource.getAllTasks();

		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
				android.R.layout.simple_list_item_1, tasks);
		setListAdapter(adapter);
	}	

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}
