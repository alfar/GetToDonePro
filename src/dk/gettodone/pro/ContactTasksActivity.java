package dk.gettodone.pro;

import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.GetToDoneProContentProvider;
import dk.gettodone.pro.data.Task;
import dk.gettodone.pro.data.TasksOpenHelper;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactTasksActivity extends ListActivity implements
		LoaderCallbacks<Cursor> {
	private static final int AGENDA_LIST_LOADER = 0x02;
	private SimpleCursorAdapter adapter;
	private Uri contactUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		contactUri = intent.getData();

		String[] uiBindFrom = { TasksOpenHelper.COLUMN_TASKS_TITLE };
		int[] uiBindTo = { R.id.doing_item_title };

		getLoaderManager().initLoader(AGENDA_LIST_LOADER, null, this);
		adapter = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.doing_item, null, uiBindFrom, uiBindTo,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task task = ContentHelper.getTask(getContentResolver(), id);
		UIHelper.finishTask(this, task);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Cursor cursor = getContentResolver().query(contactUri, null, null,
				null, null);

		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			int hasPhone = cursor
					.getInt(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone > 0) {
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String type = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
					menu.add("Call " + type + " (" + phoneNumber + ")").setIntent(
							new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
									+ phoneNumber)));
				}
				phones.close();
			}
		}
		return true;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				TasksOpenHelper.TABLE_TASKS + "." + TasksOpenHelper.COLUMN_ID,
				TasksOpenHelper.COLUMN_TASKS_TITLE,
				TasksOpenHelper.COLUMN_CONTEXTS_NAME };

		CursorLoader cursorLoader = new CursorLoader(this,
				GetToDoneProContentProvider.TASKS_URI, projection,
				TasksOpenHelper.COLUMN_TASKS_DELEGATETYPE + " = 1 AND "
						+ TasksOpenHelper.COLUMN_TASKS_DELEGATEURL
						+ " = ? AND " + TasksOpenHelper.COLUMN_TASKS_FINISHED
						+ " IS NULL", new String[] { contactUri.toString() },
				null);

		return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}
