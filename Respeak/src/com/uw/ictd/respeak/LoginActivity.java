package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static final String PREFS = "PrefsFile";
	private Button mLogInButton;
	private Button mSignUpButton;
	private EditText mPhoneNumberField;
	private String mPhoneNumber;
	private static boolean mFirstTimeUser;

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

		// Automatically detect the phone number and populates the edit text
		// field

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS, MODE_PRIVATE);
		mPhoneNumber = settings.getString("phoneNumber", null);

		// If the phone number isn't already saved in preferences, this is a
		// first-time user
		if (mPhoneNumber == null) {
			mPhoneNumber = getPhoneNumber();
			mFirstTimeUser = true;
		}
		mPhoneNumberField.setText(mPhoneNumber, TextView.BufferType.EDITABLE);

		mLogInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i;
				
				// Set the appropriate activity to be launched
				if (mFirstTimeUser) {
					i = new Intent(LoginActivity.this, TrainingActivity.class);
				} else {
					i = new Intent(LoginActivity.this, ListenActivity.class);
				}

				// Allow user to set the number
				mPhoneNumber = mPhoneNumberField.getText().toString();

				// Check that the user put in a valid phone number
				if (checkPhoneNumber(mPhoneNumber)) {
					startActivity(i);
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

	@Override
	protected void onStop() {
		super.onStop();

		// If this was a first time user, save their phone number
		if (mFirstTimeUser) {
			SharedPreferences settings = getSharedPreferences(PREFS, MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("phoneNumber", mPhoneNumber);
			mFirstTimeUser = false;
			editor.commit();
		}
	}

	// Check that the phone number is actually a valid number
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

	// Read phone number from phone
	private String getPhoneNumber() {
		TelephonyManager tMgr = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}
}
