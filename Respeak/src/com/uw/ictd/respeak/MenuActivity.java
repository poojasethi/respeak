package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_item_account_history:
			i = new Intent(this, AccountBalanceActivity.class);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
