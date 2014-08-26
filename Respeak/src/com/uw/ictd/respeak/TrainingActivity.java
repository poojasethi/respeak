package com.uw.ictd.respeak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TrainingActivity extends ActionBarActivity {
	public static int REQUEST_CODE = 100;
	
	private TextView mTrainingText;
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	private AudioRecorder mRecorder = new AudioRecorder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		mTrainingText = (TextView) findViewById(R.id.trainingText);
		mTrainingText.setText(readText());
		mTrainingText.setMovementMethod(new ScrollingMovementMethod());

		mRecordButton = (Button) findViewById(R.id.recordButtonTrain);
		mRecordImageButton = (ImageButton) findViewById(R.id.recordImageButtonTrain);
		mDoneButton = (Button) findViewById(R.id.doneButtonTrain);
		mDoneImageButton = (ImageButton) findViewById(R.id.doneImageButtonTrain);
		disableDoneButton();

		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mRecorder.isRecording()) {
					mRecorder.startRecording();
					mRecordImageButton.setBackgroundResource(R.drawable.pause_large);
					mRecordButton.setText("pause");
					enableDoneButton();
				} else {
					mRecorder.pauseRecording();
					// disableRecordButton();
					mRecordImageButton.setBackgroundResource(R.drawable.record);
					mRecordButton.setText("record");
				}
			}
		};
		mRecordButton.setOnClickListener(recordListener);
		mRecordImageButton.setOnClickListener(recordListener);

		// Stops recording
		// TODO: stop timer when you press record
		OnClickListener doneListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRecorder != null) {
					mRecorder.stopRecording();
					disableDoneButton();
					enableRecordButton();
					mRecordButton.setText("record");

					Intent i = new Intent(TrainingActivity.this,
							SubmissionActivity.class);
					i.putExtra(SubmissionActivity.EXTRA_RECORDED_FILE_NAME,
							mRecorder.getFileName());
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

	// Reads from a training text file and converts into a string
	private String readText() {
		InputStream inputStream = getResources().openRawResource(
				R.raw.training_text);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		int i;
		try {
			i = inputStream.read();
			while (i != -1) {
				outputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toString();
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
