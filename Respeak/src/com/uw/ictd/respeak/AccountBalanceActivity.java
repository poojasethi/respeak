package com.uw.ictd.respeak;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AccountBalanceActivity extends Activity {
	private Button mViewPayScaleButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_balance);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mViewPayScaleButton = (Button) findViewById(R.id.viewPayScaleButton);
		mViewPayScaleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AccountBalanceActivity.this,
						PayScaleActivity.class);
				startActivity(i);
			}
		});

		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.historyFragmentContainer);

		if (fragment == null) {
			fragment = new HistoryListFragment();
			fm.beginTransaction().add(R.id.historyFragmentContainer, fragment)
					.commit();
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
