package com.uw.ictd.respeak;

import android.os.Bundle;

public class PayScaleActivity extends MenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_scale);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
