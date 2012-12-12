package dk.gettodone.pro;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class GetToDoneProActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main);

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Fragment collectFragment = new CollectFragment();
		Fragment doingFragment = new DoingFragment();

		ActionBar.Tab collectTab = bar.newTab();
		collectTab.setText("Process");
		collectTab.setTabListener(new MainTabListener(collectFragment));		
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
	    return true;
	}
}
