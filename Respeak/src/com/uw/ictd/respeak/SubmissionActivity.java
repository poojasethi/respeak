package com.uw.ictd.respeak;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

public class SubmissionActivity extends Activity {

	static final String EXTRA_RECORDED_FILE_NAME = "com.uw.ictd.respeak.recorded_file_name";
	static final String EXTRA_RECORDED_ORIGINAL_NAME = "com.uw.ictd.respeak.original_file_name";

	private ImageButton mPlayButtonUser;
	private ImageButton mPlayButtonOriginal;
	private AudioPlayer mPlayerUser;
	private AudioPlayer mPlayerOriginal;

	private ImageButton mNoImageButton;
	private Button mNoButton;
	private ImageButton mYesImageButton;
	private Button mYesButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);

		mPlayButtonUser = (ImageButton) findViewById(R.id.playButtonUser);
		mPlayButtonOriginal = (ImageButton) findViewById(R.id.playButtonOriginal);
		mPlayButtonOriginal.setEnabled(false); // Can only play one at a time
		
		mNoImageButton = (ImageButton) findViewById(R.id.noImageButton);
		mNoButton = (Button) findViewById(R.id.noButton);
		mYesImageButton = (ImageButton) findViewById(R.id.yesImageButton);
		mYesButton = (Button) findViewById(R.id.yesButton);

		// Plays user's audio file (the recorded file)
		mPlayButtonUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// mPlayerUser.play(SubmissionActivity.this);
				// if (mPlayerUser.isPlaying()) {
				// mPlayButtonUser.setBackgroundResource(R.drawable.pause);
				// } else {
				// mPlayButtonUser.setBackgroundResource(R.drawable.play);
				// }
			}
		});

		// Plays original audio file
		mPlayButtonOriginal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// mPlayerOriginal.play(SubmissionActivity.this);
				// if (mPlayerUser.isPlaying()) {
				// mPlayButtonOriginal.setBackgroundResource(R.drawable.pause);
				// } else {
				// mPlayButtonOriginal.setBackgroundResource(R.drawable.play);
				// }
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

			} 
		};
		mYesImageButton.setOnClickListener(yesListener);
		mYesButton.setOnClickListener(yesListener);
	}
}
