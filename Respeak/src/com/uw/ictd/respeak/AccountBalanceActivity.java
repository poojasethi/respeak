package com.uw.ictd.respeak;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class AccountBalanceActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_balance);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.historyFragmentContainer);
		
		if (fragment == null) {
			fragment = new TrainingFragment();
			fm.beginTransaction().add(R.id.historyFragmentContainer, fragment).commit();
		}
	}
}
