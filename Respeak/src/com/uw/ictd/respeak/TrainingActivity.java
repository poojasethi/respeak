package com.uw.ictd.respeak;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

public class TrainingActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment_container);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.singleFragmentContainer);
		
		if (fragment == null) {
			fragment = new TrainingFragment();
			fm.beginTransaction().add(R.id.singleFragmentContainer, fragment).commit();
		}
	}
}
