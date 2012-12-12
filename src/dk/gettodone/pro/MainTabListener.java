package dk.gettodone.pro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;

public class MainTabListener implements TabListener {
	
	public Fragment fragment = null;
	
	public MainTabListener(Fragment fragment)
	{
		this.fragment = fragment;
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}
