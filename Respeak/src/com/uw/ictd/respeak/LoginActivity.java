package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button mLogInButton;
	private Button mSignUpButton;
	private EditText mPhoneNumberField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mLogInButton = (Button) findViewById(R.id.logInButton);
		mSignUpButton = (Button) findViewById(R.id.signUpButton);
		mPhoneNumberField = (EditText) findViewById(R.id.phoneNumberField);

		// Hides keyboard until user selects edit text (phone number field)
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		mLogInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, ListenActivity.class);
				String phoneNumber = mPhoneNumberField.getText().toString();

				// Check that the user put in a phone number
				if (checkPhoneNumber(phoneNumber)) {
					i.putExtra(ListenActivity.EXTRA_PHONE_NUMBER, phoneNumber);
					startActivityForResult(i, 0);
				}
			}
		});

		// TODO: implement sign up activity using fragments
		mSignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this,
						TrainingActivity.class);
				startActivity(i);
			}
		});

	}

	// TODO: check that the phone number is actually a valid number
	private boolean checkPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() < 10) {
			Toast toast = Toast.makeText(this,
					R.string.invalid_phone_number_toast, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return false;
		}
		return true;
	}
}
