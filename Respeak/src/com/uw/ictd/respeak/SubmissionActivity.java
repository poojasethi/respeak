package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SubmissionActivity extends Activity {

	static final String EXTRA_RECORDED_FILE_NAME = "com.uw.ictd.respeak.recorded_file_name";
	static final String EXTRA_ORIGINAL_FILE_NAME = "com.uw.ictd.respeak.original_file_name";

	private ImageButton mPlayButtonRecorded;
	private ImageButton mPlayButtonOriginal;
	private AudioPlayer mPlayerRecorded;
	private AudioPlayer mPlayerOriginal = new AudioPlayer();

	private ImageButton mNoImageButton;
	private Button mNoButton;
	private ImageButton mYesImageButton;
	private Button mYesButton;
	
	private String mRecordedFileName;
	private String mOriginalFileName;
	private Uri mRecordedFile;
	private Uri mOriginalFile;

	private Handler mHandler = new Handler();;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);

		mPlayButtonRecorded = (ImageButton) findViewById(R.id.playButtonUser);
		mPlayButtonOriginal = (ImageButton) findViewById(R.id.playButtonOriginal);
		
		// You must listen to your own audio file (the recording) at least once
		// before playing the original 
		mPlayButtonOriginal.setEnabled(false);

		mNoImageButton = (ImageButton) findViewById(R.id.noImageButton);
		mNoButton = (Button) findViewById(R.id.noButton);
		mYesImageButton = (ImageButton) findViewById(R.id.yesImageButton);
		mYesButton = (Button) findViewById(R.id.yesButton);
		
		// Get the location of the recorded and original file
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mRecordedFileName = extras.getString(EXTRA_RECORDED_FILE_NAME);
			mRecordedFile = Uri.parse(mRecordedFileName);
			mPlayerRecorded = new AudioPlayer(mRecordedFile);
		}

		// Plays user's audio file (the recorded file)
		mPlayButtonRecorded.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerRecorded.play(SubmissionActivity.this);
				if (mPlayerRecorded.isPlaying()) {
					mPlayButtonRecorded.setBackgroundResource(R.drawable.pause);
					mPlayButtonOriginal.setEnabled(false);
				} else {
					mPlayButtonRecorded.setBackgroundResource(R.drawable.play);
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
					mPlayButtonRecorded.setEnabled(false);
				} else {
					mPlayButtonOriginal.setBackgroundResource(R.drawable.play);
					mPlayButtonRecorded.setEnabled(true);
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
