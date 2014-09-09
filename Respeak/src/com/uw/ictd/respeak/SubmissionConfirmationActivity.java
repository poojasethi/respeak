package com.uw.ictd.respeak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SubmissionConfirmationActivity extends MenuActivity {
	
	private Button mViewPayScaleButton;
	private Button mMoreRespeaksButton;
	private Button mAccountBalanceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission_confirmation);
		
		mViewPayScaleButton = (Button) findViewById(R.id.viewPayScaleButton);
		mMoreRespeaksButton = (Button) findViewById(R.id.moreRespeaksButton);
		mAccountBalanceButton = (Button) findViewById(R.id.viewAccountButton);
		
		mViewPayScaleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SubmissionConfirmationActivity.this, PayScaleActivity.class);
				startActivity(i);
			}
		});
		
		mMoreRespeaksButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SubmissionConfirmationActivity.this, ListenActivity.class);
				startActivity(i);
			}
		});
		
		mAccountBalanceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SubmissionConfirmationActivity.this, AccountBalanceActivity.class);
				startActivity(i);
			}
		});
		
		
	}
}
