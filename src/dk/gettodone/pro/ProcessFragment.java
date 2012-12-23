package dk.gettodone.pro;

import java.util.List;

import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.Context;
import dk.gettodone.pro.data.GetToDoneProContentProvider;
import dk.gettodone.pro.data.Task;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
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
	private Task activeTask;

	private void showNextProcessableTask() {
		showNextProcessableTask(getView());
	}

	private void showNextProcessableTask(View view) {
		if (view != null) {
			activeTask = ContentHelper.getNextProcessableTask(getActivity()
					.getContentResolver());

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
				final List<Context> contexts = ContentHelper
						.getContexts(getActivity().getContentResolver());
				contexts.add(new Context(-1, getResources().getString(
						R.string.add_context)));

				CharSequence[] contextItems = new CharSequence[contexts.size() - 1];

				for (int i = 0; i < contexts.size() - 1; i++) {
					contextItems[i] = contexts.get(i).toString();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Pick a context");
				builder.setItems(contextItems,
						new AlertDialog.OnClickListener() {

							public void onClick(DialogInterface dlg, int which) {
								Context chosenContext = contexts.get(which);
								if (chosenContext.getId() == -1) {
									AlertDialog.Builder newContext = new AlertDialog.Builder(
											getActivity());
									newContext.setTitle("Create a new context");
									final EditText input = new EditText(
											getActivity());
									newContext.setView(input);
									newContext
											.setPositiveButton(
													"Create",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															ContentHelper
																	.processTaskToNewContext(
																			getActivity()
																					.getContentResolver(),
																			activeTask
																					.getId(),
																			input.getText()
																					.toString());

															Toast.makeText(
																	getActivity(),
																	"Sent to context",
																	1000)
																	.show();
															showNextProcessableTask();
														}
													});
									newContext
											.setNegativeButton("Cancel", null);
									newContext.show();
								} else {
									long id = chosenContext.getId();

									ContentHelper.processTaskToContext(
											getActivity().getContentResolver(),
											activeTask.getId(), id);

									Toast.makeText(getActivity(),
											"Sent to Context", 1000).show();
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
				AlertDialog.Builder newContext = new AlertDialog.Builder(
						getActivity());
				newContext.setTitle("What's the next action?");
				final EditText input = new EditText(getActivity());
				input.setText(activeTask.getTitle());
				newContext.setView(input);
				newContext.setPositiveButton("Create",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ContentHelper.processTaskToProject(
										getActivity().getContentResolver(),
										activeTask.getId(), input.getText()
												.toString());

								Toast.makeText(getActivity(),
										"Project created", 1000).show();
								showNextProcessableTask();
							}
						});
				newContext.setNegativeButton("Cancel", null);
				newContext.show();
			}
		});

		ImageButton btnDoNow = (ImageButton) result
				.findViewById(R.id.button_process_do);

		btnDoNow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIHelper.finishTask(getActivity(), activeTask);
			}
		});

		ImageButton btnSomeday = (ImageButton) result
				.findViewById(R.id.button_process_someday);

		btnSomeday.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ContentHelper.processTaskToSomeday(getActivity()
						.getContentResolver(), activeTask.getId());

				Toast.makeText(getActivity(), "Some day...", 1000).show();
				showNextProcessableTask();
			}
		});

		ImageButton btnTrash = (ImageButton) result
				.findViewById(R.id.button_process_trash);
		btnTrash.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ContentHelper.deleteTask(getActivity().getContentResolver(),
						activeTask.getId());
				Toast.makeText(getActivity(), "Trashed!", 1000).show();
				showNextProcessableTask();
			}
		});

		showNextProcessableTask(result);
		return result;
	}

	TaskContentObserver tasksWatcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tasksWatcher = new TaskContentObserver(new Handler());
		getActivity().getContentResolver().registerContentObserver(
				GetToDoneProContentProvider.TASKS_URI, false, tasksWatcher);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().getContentResolver().unregisterContentObserver(
				tasksWatcher);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getContentResolver().registerContentObserver(
				GetToDoneProContentProvider.TASKS_URI, false, tasksWatcher);
	}


	public class TaskContentObserver extends ContentObserver {

		public TaskContentObserver(Handler handler) {
			super(handler);

		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			showNextProcessableTask();
		}
	}
}
