package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RecordActivity extends MenuActivity {
	private static final int REQUEST_CODE = 0;
	
	private ImageView mRecorderPic;
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	
	private AudioRecorder mRecorder = new AudioRecorder();
	private Chronometer mChronometer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mRecorderPic = (ImageView) findViewById(R.id.recorderPic);
		mRecordButton = (Button) findViewById(R.id.recordButton);
		mRecordImageButton = (ImageButton) findViewById(R.id.recordImageButton);
		mDoneButton = (Button) findViewById(R.id.doneButton);
		mDoneImageButton = (ImageButton) findViewById(R.id.doneImageButton);
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		
		disableDoneButton();
		
		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				mRecorder.startRecording();
				
				mChronometer.setBase(SystemClock.elapsedRealtime());
				mChronometer.start();
				
				// Changes color of mic for API level 16 and above
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
					mRecorderPic.setImageResource(R.drawable.red_mic);
				}
			    
				disableRecordButton();
				enableDoneButton();
			}
		};
		mRecordButton.setOnClickListener(recordListener);
		mRecordImageButton.setOnClickListener(recordListener);
		
		
		// Stops recording
		// TODO: stop timer when you press record
		OnClickListener doneListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mRecorder != null) {
					mRecorder.stopRecording();
					
					mChronometer.stop();
					
					// Changes color of mic for API level 16 and above
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
						mRecorderPic.setImageResource(R.drawable.blue_mic);
					}
					 
					disableDoneButton();
					enableRecordButton();
					
					Intent i = new Intent(RecordActivity.this, SubmissionActivity.class);
					i.putExtra(SubmissionActivity.EXTRA_RECORDED_FILE_NAME, mRecorder.getFileName());
					Bundle bundle = getIntent().getExtras();
					if (bundle != null) {
						i.putExtras(bundle);
					}
					startActivityForResult(i, REQUEST_CODE);
				}
			}
		};
		mDoneButton.setOnClickListener(doneListener);
		mDoneImageButton.setOnClickListener(doneListener);
	}
	
	private void disableDoneButton() {
		mDoneButton.setEnabled(false);
		mDoneButton.setTextColor(getResources().getColor(R.color.light_gray));
		mDoneImageButton.setEnabled(false);
		mDoneImageButton.setBackgroundResource(R.drawable.done_gray);
	}
	
	private void enableDoneButton() {
		mDoneButton.setEnabled(true);
		mDoneButton.setTextColor(getResources().getColor(R.color.light_blue));
		mDoneImageButton.setEnabled(true);
		mDoneImageButton.setBackgroundResource(R.drawable.done_black);
	}
	
	private void disableRecordButton() {
		mRecordButton.setEnabled(false);
		mRecordButton.setTextColor(getResources().getColor(R.color.light_gray));
		mRecordImageButton.setEnabled(false);
		mRecordImageButton.setBackgroundResource(R.drawable.record_gray);
	}
	
	private void enableRecordButton() {
		mRecordButton.setEnabled(true);
		mRecordButton.setTextColor(getResources().getColor(R.color.light_blue));
		mRecordImageButton.setEnabled(true);
		mRecordImageButton.setBackgroundResource(R.drawable.record);
	}
	
	@Override
    public void onPause() {
        super.onPause();
        mRecorder.stopRecording();
    }
}
