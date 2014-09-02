package com.uw.ictd.respeak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TrainingFragment extends Fragment {
	public static int REQUEST_CODE = 100;
	private static final String DIALOG_TRAINING = "training welcome";
	
	private TextView mTrainingText;
	private Button mRecordButton;
	private ImageButton mRecordImageButton;
	private Button mDoneButton;
	private ImageButton mDoneImageButton;
	private TextView mYourRecording;
	private TextView mAudioCurrentDurationTraining;
	private TextView mAudioTotalDurationTraining;
	private SeekBar mAudioProgressBarTraining;
	private Button mRecordAgainButton;
	private Button mSubmitButton;
	private ImageButton mPlayButtonTraining;
	private Uri mRecordedFile;
	private AudioRecorder mRecorder = new AudioRecorder();
	private AudioPlayer mPlayer;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getActivity().getFragmentManager();
		TrainingDialogFragment dialog = new TrainingDialogFragment();
		dialog.show(fm, DIALOG_TRAINING);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_training, parent, false);

		mTrainingText = (TextView) v.findViewById(R.id.trainingText);
		mTrainingText.setText(readText());
		mTrainingText.setMovementMethod(new ScrollingMovementMethod());

		mRecordButton = (Button) v.findViewById(R.id.recordButtonTrain);
		mRecordImageButton = (ImageButton) v
				.findViewById(R.id.recordImageButtonTrain);
		mDoneButton = (Button) v.findViewById(R.id.doneButtonTrain);
		mDoneImageButton = (ImageButton) v
				.findViewById(R.id.doneImageButtonTrain);
		disableDoneButton();

		mYourRecording = (TextView) v.findViewById(R.id.yourRecording);
		mAudioCurrentDurationTraining = (TextView) v
				.findViewById(R.id.audioCurrentDurationTraining);
		mAudioTotalDurationTraining = (TextView) v
				.findViewById(R.id.audioTotalDurationTraining);
		mAudioProgressBarTraining = (SeekBar) v
				.findViewById(R.id.audioProgressBarTraining);
		mPlayButtonTraining = (ImageButton) v
				.findViewById(R.id.playButtonTraining);
		mRecordAgainButton = (Button) v.findViewById(R.id.recordAgainButton);
		mSubmitButton = (Button) v.findViewById(R.id.submitButton);
		hidePlayBack();

		// Starts recording
		// TODO: start timer when you press record
		OnClickListener recordListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRecorder.startRecording();
				mTrainingText
						.setBackgroundResource(R.color.light_red_background);
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
					mTrainingText.scrollTo(0, 0);
					disableDoneButton();
					enableRecordButton();
					hideRecording();
					showPlayBack();
					mRecordAgainButton.setVisibility(View.GONE);
					mSubmitButton.setVisibility(View.GONE);
					
					// Get the name of the recorded file and initialize the audio player
					mRecordedFile = Uri.parse(mRecorder.getFileName());
					mPlayer = new AudioPlayer(mRecordedFile);
				
					// Intent i = new Intent(getActivity(),
					// SubmissionActivity.class);
					// i.putExtra(SubmissionActivity.EXTRA_RECORDED_FILE_NAME,
					// mRecorder.getFileName());
					// Bundle bundle = getActivity().getIntent().getExtras();
					// if (bundle != null) {
					// i.putExtras(bundle);
					// }
					// startActivityForResult(i, REQUEST_CODE);
				}
			}
		};
		mDoneButton.setOnClickListener(doneListener);
		mDoneImageButton.setOnClickListener(doneListener);

		// Allows user to play/pause recording
		mPlayButtonTraining.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.play(getActivity());
				mRecordAgainButton.setVisibility(View.VISIBLE);
				mSubmitButton.setVisibility(View.VISIBLE);
				if (mPlayer.isPlaying()) {
					// Update progress bar and total time
					updateProgressBar();
					long totalDuration = mPlayer.getDuration();
					mAudioTotalDurationTraining.setText(TimeConverter
							.milliSecondsToTimer(totalDuration));

					mPlayButtonTraining.setBackgroundResource(R.drawable.pause);
				} else {
					mPlayButtonTraining.setBackgroundResource(R.drawable.play);
				}
			}
		});

		// Set listener on audio progress bar that tracks user's touch
		OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Progress bar no longer updates
				mHandler.removeCallbacks(mUpdateTimeTask);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpdateTimeTask);
				int totalDuration = (int) mPlayer.getDuration();
				int currentPosition = TimeConverter.progressToTimer(
						seekBar.getProgress(), totalDuration);

				// Move audio player forward or backward appropriate number of
				// seconds
				mPlayer.seekTo(currentPosition);

				// Update timers
				updateProgressBar();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		};
		mAudioProgressBarTraining
				.setOnSeekBarChangeListener(seekBarChangeListener);

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

	private void hidePlayBack() {
		mYourRecording.setVisibility(View.GONE);
		mAudioCurrentDurationTraining.setVisibility(View.GONE);
		mAudioTotalDurationTraining.setVisibility(View.GONE);
		mAudioProgressBarTraining.setVisibility(View.GONE);
		mPlayButtonTraining.setVisibility(View.GONE);
		mRecordAgainButton.setVisibility(View.GONE);
		mSubmitButton.setVisibility(View.GONE);
	}

	private void hideRecording() {
		mRecordButton.setVisibility(View.GONE);
		mRecordImageButton.setVisibility(View.GONE);
		mDoneButton.setVisibility(View.GONE);
		mDoneImageButton.setVisibility(View.GONE);
	}

	private void showPlayBack() {
		mYourRecording.setVisibility(View.VISIBLE);
		mAudioCurrentDurationTraining.setVisibility(View.VISIBLE);
		mAudioTotalDurationTraining.setVisibility(View.VISIBLE);
		mAudioProgressBarTraining.setVisibility(View.VISIBLE);
		mPlayButtonTraining.setVisibility(View.VISIBLE);
		mRecordAgainButton.setVisibility(View.VISIBLE);
		mSubmitButton.setVisibility(View.VISIBLE);
	}

	private void showRecording() {
		mRecordButton.setVisibility(View.VISIBLE);
		mRecordImageButton.setVisibility(View.VISIBLE);
		mDoneButton.setVisibility(View.VISIBLE);
		mDoneImageButton.setVisibility(View.VISIBLE);
	}

	// Update timer and audio progress bar
	private void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	// Thread to update timers
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long currentDuration = mPlayer.getCurrentPosition();
			long totalDuration = mPlayer.getDuration();

			// Update the current audio duration label
			mAudioCurrentDurationTraining.setText(TimeConverter
					.milliSecondsToTimer(currentDuration));

			// Update the progress bar
			int progress = TimeConverter.getProgressPercentage(currentDuration,
					totalDuration);
			mAudioProgressBarTraining.setProgress(progress);

			// Run this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		mRecorder.stopRecording();
		mPlayer.stop();
	}
}
