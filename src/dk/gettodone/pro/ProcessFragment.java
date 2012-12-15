package dk.gettodone.pro;

import dk.gettodone.pro.data.ITasksDataSource;
import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TaskChangedListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessFragment extends Fragment {
	private final class TaskCreatedListener implements TaskChangedListener {
		private ProcessFragment owner;	

		public TaskCreatedListener(ProcessFragment owner) {
			this.owner = owner;
		}

		public void onTaskChanged(Task task) {
			owner.showNextProcessableTask();
		}
	}

	private ITasksDataSource datasource;	

	public ProcessFragment(ITasksDataSource ds) {
		this.datasource = ds;
	}

	private void showNextProcessableTask() {
		showNextProcessableTask(getView());
	}

	private void showNextProcessableTask(View view) {
		Task task = datasource.getNextProcessableTask();

		if (task != null) { 
			TextView tv = (TextView)view.findViewById(R.id.textViewTaskTitle);
			tv.setText(datasource.getNextProcessableTask().getTitle());
		}
		else
		{
			TextView tv = (TextView)view.findViewById(R.id.textViewTaskTitle);
			tv.setText("Nothing to process!");			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.process, container, false);

		ImageButton btnContext = (ImageButton)result.findViewById(R.id.button_process_context);
		btnContext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getActivity(), "Send to Context", 1000).show();
			}
		});

		ImageButton btnTrash = (ImageButton)result.findViewById(R.id.button_process_trash);
		btnTrash.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getActivity(), "Throw away!", 1000).show();
			}
		});

		showNextProcessableTask(result);
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.datasource.setOnTaskCreatedListener(new TaskCreatedListener(this));		
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
