package com.uw.ictd.respeak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RecordFragment extends Fragment {
	
	private ImageView mRecorderPic;
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	
	private AudioRecorder mRecorder = new AudioRecorder();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_record, parent, false);
		
		mRecorderPic = (ImageView) v.findViewById(R.id.recorderPic);
		mRecordButton = (Button) v.findViewById(R.id.recordButton);
		mRecordImageButton = (ImageButton) v.findViewById(R.id.recordImageButton);
		mDoneButton = (Button) v.findViewById(R.id.doneButton);
		mDoneImageButton = (ImageButton) v.findViewById(R.id.doneImageButton);
		
		disableDoneButton();
		
		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				mRecorder.startRecording();
				
				// Changes color of mic for API level 16 and above
				if (android.os.Build.VERSION.SDK_INT > 15) {
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
					
					// Changes color of mic for API level 16 and above
					if (android.os.Build.VERSION.SDK_INT > 15) {
						mRecorderPic.setImageResource(R.drawable.blue_mic);
					}
					 
					disableDoneButton();
					enableRecordButton();
				}
			}
		};
		mDoneButton.setOnClickListener(doneListener);
		mDoneImageButton.setOnClickListener(doneListener);
		
		return v;
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
