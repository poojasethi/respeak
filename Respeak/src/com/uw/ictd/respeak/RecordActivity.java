package com.uw.ictd.respeak;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class RecordActivity extends Activity {
	
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	
	private AudioRecorder mRecorder = new AudioRecorder();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		mRecordButton = (Button) findViewById(R.id.recordButton);
		mRecordImageButton = (ImageButton) findViewById(R.id.recordImageButton);
		mDoneButton = (Button) findViewById(R.id.doneButton);
		mDoneImageButton = (ImageButton) findViewById(R.id.doneImageButton);
		
		disableDoneButton();
		
		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				mRecorder.startRecording();
				
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
					
					disableDoneButton();
					enableRecordButton();
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
        mRecorder.stop();
    }
}
