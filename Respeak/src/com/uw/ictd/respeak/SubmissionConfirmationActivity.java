package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SubmissionConfirmationActivity extends Activity {
	
	private Button mViewPayScaleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission_confirmation);
		
		mViewPayScaleButton = (Button) findViewById(R.id.viewPayScaleButton);
		
		mViewPayScaleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SubmissionConfirmationActivity.this, PayScaleActivity.class);
				startActivity(i);
			}
		});
	}
}
