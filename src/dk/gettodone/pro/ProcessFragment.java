package dk.gettodone.pro;

import java.util.ArrayList;

import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.ITasksDataSource;
import dk.gettodone.pro.data.TasksOpenHelper;
import dk.gettodone.pro.data.GetToDoneProContentProvider;
import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TaskChangedListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
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
	private Task activeTask;

	public void setDataSource(ITasksDataSource ds) {
		this.datasource = ds;
		this.datasource.setOnTaskCreatedListener(new TaskCreatedListener(this));
		showNextProcessableTask();
	}

	private void showNextProcessableTask() {
		showNextProcessableTask(getView());
	}

	private void showNextProcessableTask(View view) {
		if (datasource != null && view != null) {

			activeTask = datasource.getNextProcessableTask();

			TextView tv = (TextView) view.findViewById(R.id.textViewTaskTitle);
			TableLayout tableProcessOptions = (TableLayout) view
					.findViewById(R.id.tableProcessOptions);
			if (activeTask != null) {
				tv.setText(activeTask.getTitle());
				tableProcessOptions.setVisibility(View.VISIBLE);
			} else {
				tv.setText("Nothing to process!");
				tableProcessOptions.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.process, container, false);

		ImageButton btnContext = (ImageButton) result
				.findViewById(R.id.button_process_context);
		btnContext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Cursor contexts = getActivity().getContentResolver().query(
						GetToDoneProContentProvider.CONTEXTS_URI,
						new String[] { TasksOpenHelper.COLUMN_ID,
								TasksOpenHelper.COLUMN_CONTEXTS_NAME }, null,
						null, TasksOpenHelper.COLUMN_CONTEXTS_NAME);

				ArrayList<String> contextList = new ArrayList<String>();
				final ArrayList<Integer> contextIds = new ArrayList<Integer>();

				contexts.moveToFirst();
				while (!contexts.isAfterLast()) {
					contextIds.add(contexts.getInt(0));
					contextList.add(contexts.getString(1));
					contexts.moveToNext();
				}

				contextList.add(getResources().getString(R.string.add_context));

				final CharSequence[] items = new CharSequence[contextList
						.size()];
				contextList.toArray(items);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Pick a context");
				builder.setItems(items, new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dlg, int which) {
						if (which == contextIds.size()) {
							AlertDialog.Builder newContext = new AlertDialog.Builder(getActivity());
							newContext.setTitle("Create a new context");
							final EditText input = new EditText(getActivity());
							newContext.setView(input);
							newContext.setPositiveButton("Create", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
										ContentHelper.processTaskToNewContext(getActivity().getContentResolver(), activeTask.getId(), input.getText().toString());
									
										Toast.makeText(getActivity(), "Sent to context",
												1000).show();
										showNextProcessableTask();
								}});
							newContext.setNegativeButton("Cancel", null);
							newContext.show();
						} else {
							int id = contextIds.get(which);

							ContentHelper.processTaskToContext(getActivity()
									.getContentResolver(), activeTask.getId(),
									id);

							Toast.makeText(getActivity(), "Sent to Context",
									1000).show();
							showNextProcessableTask();
						}
					}

				}).show();
			}
		});

		ImageButton btnProject = (ImageButton) result
				.findViewById(R.id.button_process_project);

		btnProject.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put(TasksOpenHelper.COLUMN_CONTEXTS_NAME, "Home");
				getActivity().getContentResolver().insert(
						GetToDoneProContentProvider.CONTEXTS_URI, values);

				values = new ContentValues();
				values.put(TasksOpenHelper.COLUMN_CONTEXTS_NAME, "Work");
				getActivity().getContentResolver().insert(
						GetToDoneProContentProvider.CONTEXTS_URI, values);
			}
		});

		ImageButton btnTrash = (ImageButton) result
				.findViewById(R.id.button_process_trash);
		btnTrash.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				datasource.deleteTask(activeTask);
				Toast.makeText(getActivity(), "Trashed!", 1000).show();
				showNextProcessableTask();
			}
		});

		showNextProcessableTask(result);
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
