package com.uw.ictd.respeak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TrainingFragment extends Fragment {
	public static int REQUEST_CODE = 100;
	private static final String DIALOG_TRAINING = "training welcome";

	private TextView mTrainingText;
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	private AudioRecorder mRecorder = new AudioRecorder();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getActivity().getFragmentManager();
		TrainingDialogFragment dialog = new TrainingDialogFragment();
		dialog.show(fm, DIALOG_TRAINING);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_training, parent, false);

		mTrainingText = (TextView) v.findViewById(R.id.trainingText);
		mTrainingText.setText(readText());
		mTrainingText.setMovementMethod(new ScrollingMovementMethod());

		mRecordButton = (Button) v.findViewById(R.id.recordButtonTrain);
		mRecordImageButton = (ImageButton) v.findViewById(R.id.recordImageButtonTrain);
		mDoneButton = (Button) v.findViewById(R.id.doneButtonTrain);
		mDoneImageButton = (ImageButton) v.findViewById(R.id.doneImageButtonTrain);
		disableDoneButton();

		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRecorder.startRecording();
				mTrainingText.setBackgroundResource(R.color.light_red_background);
				disableRecordButton();
				enableDoneButton();
				// if (!mRecorder.isRecording()) {
				// mRecorder.startRecording();
				// mRecordImageButton
				// .setBackgroundResource(R.drawable.pause_large);
				// mRecordButton.setText("pause");
				// enableDoneButton();
				// } else {
				// mRecorder.pauseRecording();
				// // disableRecordButton();
				// mRecordImageButton.setBackgroundResource(R.drawable.record);
				// mRecordButton.setText("record");
				// }
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
					mTrainingText.setBackgroundResource(R.color.lighter_gray);
					disableDoneButton();
					enableRecordButton();

					Intent i = new Intent(getActivity(),
							SubmissionActivity.class);
					i.putExtra(SubmissionActivity.EXTRA_RECORDED_FILE_NAME,
							mRecorder.getFileName());
					Bundle bundle = getActivity().getIntent().getExtras();
					if (bundle != null) {
						i.putExtras(bundle);
					}
					startActivityForResult(i, REQUEST_CODE);
				}
			}
		};
		mDoneButton.setOnClickListener(doneListener);
		mDoneImageButton.setOnClickListener(doneListener);
		
		return v;
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
		mRecorder.stopRecording();
	}
}
