package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
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
		case android.R.id.home:
			if (NavUtils.getParentActivityIntent(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
				Log.d("BACK", "Back button pressed" );
			} else {
				this.onBackPressed();
			}
			return true;
		case R.id.menu_item_settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_item_account_history:
			i = new Intent(this, AccountBalanceActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_item_pay_scale:
			i = new Intent(this, PayScaleActivity.class);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
