package dk.gettodone.pro;

import dk.gettodone.pro.data.ITasksDataSource;
import dk.gettodone.pro.data.TasksDataSource;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GetToDoneProActivity extends Activity {
	ITasksDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new TasksDataSource(this);
		datasource.resume();

		setContentView(R.layout.main);

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ProcessFragment processFragment = new ProcessFragment();
		processFragment.setDataSource((ITasksDataSource)datasource);
		Fragment doingFragment = new DoingFragment();

		ActionBar.Tab collectTab = bar.newTab();
		collectTab.setText("Process");
		collectTab.setTabListener(new MainTabListener(processFragment));
		bar.addTab(collectTab);

		ActionBar.Tab doTab = bar.newTab();
		doTab.setText("Do");
		doTab.setTabListener(new MainTabListener(doingFragment));
		bar.addTab(doTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);

		View collectView = menu.findItem(R.id.item_collect).getActionView();
		ImageButton buttonCollect = (ImageButton) collectView
				.findViewById(R.id.buttonCollect);

		EditText textCollect = (EditText) collectView
				.findViewById(R.id.textCollect);

		textCollect.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					return ((View) v.getParent()).findViewById(
							R.id.buttonCollect).performClick();
				}
				return false;
			}
		});

		buttonCollect.setOnClickListener(new CollectClickListener(datasource));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item_collect) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			View collectView = item.getActionView();
			imm.showSoftInput(collectView.findViewById(R.id.textCollect),
					InputMethodManager.SHOW_IMPLICIT);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.resume();
	}

	private class CollectClickListener implements OnClickListener {
		private ITasksDataSource datasource;

		public CollectClickListener(ITasksDataSource datasource) {
			super();
			this.datasource = datasource;
		}

		public void onClick(View v) {
			View collectView = (View) v.getParent();
			EditText textCollect = (EditText) collectView
					.findViewById(R.id.textCollect);
			datasource.createTask(textCollect.getText().toString());
			textCollect.setText("");
		}
	}
}
