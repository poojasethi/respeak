package com.uw.ictd.respeak;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_single_fragment_container);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.singleFragmentContainer);
		
		if (fragment == null) {
			fragment = new SettingsFragment();
			fm.beginTransaction().add(R.id.singleFragmentContainer, fragment).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
