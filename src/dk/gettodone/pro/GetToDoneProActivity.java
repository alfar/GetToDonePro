package dk.gettodone.pro;

import dk.gettodone.pro.data.ITasksDataSource;
import dk.gettodone.pro.data.TasksDataSource;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

		Fragment processFragment = new ProcessFragment(datasource);
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
		Button buttonCollect = (Button)collectView.findViewById(R.id.buttonCollect);

		buttonCollect.setOnClickListener(new CollectClickListener(datasource));	    
		return true;
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
			View collectView = (View)v.getParent();
			EditText textCollect = (EditText)collectView.findViewById(R.id.textCollect);
			datasource.createTask(textCollect.getText().toString());
			textCollect.setText("");
		}
	}
}
