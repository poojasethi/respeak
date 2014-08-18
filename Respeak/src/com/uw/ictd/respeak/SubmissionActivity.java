package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SubmissionActivity extends Activity {

	static final String EXTRA_RECORDED_FILE_NAME = "com.uw.ictd.respeak.recorded_file_name";
	static final String EXTRA_RECORDED_ORIGINAL_NAME = "com.uw.ictd.respeak.original_file_name";

	private ImageButton mPlayButtonUser;
	private ImageButton mPlayButtonOriginal;
	private AudioPlayer mPlayerUser = new AudioPlayer();
	private AudioPlayer mPlayerOriginal = new AudioPlayer();

	private ImageButton mNoImageButton;
	private Button mNoButton;
	private ImageButton mYesImageButton;
	private Button mYesButton;

	private Handler mHandler = new Handler();;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);

		mPlayButtonUser = (ImageButton) findViewById(R.id.playButtonUser);
		mPlayButtonOriginal = (ImageButton) findViewById(R.id.playButtonOriginal);
		
		// You must listen to your own audio file (the recording) at least once
		// before playing the original 
		mPlayButtonOriginal.setEnabled(false);

		mNoImageButton = (ImageButton) findViewById(R.id.noImageButton);
		mNoButton = (Button) findViewById(R.id.noButton);
		mYesImageButton = (ImageButton) findViewById(R.id.yesImageButton);
		mYesButton = (Button) findViewById(R.id.yesButton);
		

		// Plays user's audio file (the recorded file)
		mPlayButtonUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerUser.play(SubmissionActivity.this);
				if (mPlayerUser.isPlaying()) {
					mPlayButtonUser.setBackgroundResource(R.drawable.pause);
					mPlayButtonOriginal.setEnabled(false);
				} else {
					mPlayButtonUser.setBackgroundResource(R.drawable.play);
					mPlayButtonOriginal.setEnabled(true);
				}
			}
		});

		// Plays original audio file
		mPlayButtonOriginal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerOriginal.play(SubmissionActivity.this);
				if(mPlayerOriginal.isPlaying()) {
					mPlayButtonOriginal.setBackgroundResource(R.drawable.pause);
					mPlayButtonUser.setEnabled(false);
				} else {
					mPlayButtonOriginal.setBackgroundResource(R.drawable.play);
					mPlayButtonUser.setEnabled(true);
				}
			}
		});

		// Press no, record again
		OnClickListener noListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		};
		mNoImageButton.setOnClickListener(noListener);
		mNoButton.setOnClickListener(noListener);

		// Press yes, submit
		OnClickListener yesListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SubmissionActivity.this,
						SubmissionConfirmationActivity.class);
				startActivity(i);
			}
		};
		mYesImageButton.setOnClickListener(yesListener);
		mYesButton.setOnClickListener(yesListener);
	}
}
