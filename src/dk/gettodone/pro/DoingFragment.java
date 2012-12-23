package dk.gettodone.pro;

import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.GetToDoneProContentProvider;
import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TasksOpenHelper;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DoingFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.doing, container, false);
	}

	public void refreshListView() {
		ListAdapter adapter = this.getListAdapter();
		if (adapter instanceof ArrayAdapter<?>) {
			ArrayAdapter<?> list = (ArrayAdapter<?>) adapter;
			list.notifyDataSetChanged();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task task = ContentHelper.getTask(getActivity().getContentResolver(), id);
		UIHelper.finishTask(getActivity(), task);
	}

	private static final int TASK_LIST_LOADER = 0x01;
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] uiBindFrom = { TasksOpenHelper.COLUMN_TASKS_TITLE,
				TasksOpenHelper.COLUMN_CONTEXTS_NAME };
		int[] uiBindTo = { R.id.doing_item_title, R.id.doing_item_context };

		getLoaderManager().initLoader(TASK_LIST_LOADER, null, this);
		adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), R.layout.doing_item,
				null, uiBindFrom, uiBindTo,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				TasksOpenHelper.TABLE_TASKS + "." + TasksOpenHelper.COLUMN_ID,
				TasksOpenHelper.COLUMN_TASKS_TITLE,
				TasksOpenHelper.COLUMN_CONTEXTS_NAME };

		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				Uri.withAppendedPath(GetToDoneProContentProvider.TASKS_URI,
						"/context"), projection, null, null, null);
		return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
}
