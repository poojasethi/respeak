package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	private Button mLogInButton;
	private Button mSignUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mLogInButton = (Button) findViewById(R.id.logInButton);
		mSignUpButton = (Button) findViewById(R.id.signUpButton);
		
		mLogInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, ListenActivity.class);
				startActivityForResult(i, 0);
			}
		});	
		
		// TODO: implement sign up activity
		mSignUpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
	}
}
