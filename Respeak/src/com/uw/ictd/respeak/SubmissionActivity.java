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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SubmissionActivity extends Activity {

	static final String EXTRA_RECORDED_FILE_NAME = "com.uw.ictd.respeak.recorded_file_name";
	static final String EXTRA_ORIGINAL_FILE_NAME = "com.uw.ictd.respeak.original_file_name";

	private ImageButton mPlayButtonRecorded;
	private ImageButton mPlayButtonOriginal;
	private AudioPlayer mPlayerRecorded;
	private AudioPlayer mPlayerOriginal;
	private ImageButton mNoImageButton;
	private Button mNoButton;
	private ImageButton mYesImageButton;
	private Button mYesButton;
	private Uri mRecordedFile;
	private Uri mOriginalFile;
	private Handler mHandler = new Handler();
	private SeekBar mAudioProgressBarRecorded;
	private SeekBar mAudioProgressBarOriginal;
	private TextView mAudioCurrentDurationRecorded;
	private TextView mAudioTotalDurationRecorded;
	private TextView mAudioCurrentDurationOriginal;
	private TextView mAudioTotalDurationOriginal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);

		mPlayButtonRecorded = (ImageButton) findViewById(R.id.playButtonUser);
		mPlayButtonOriginal = (ImageButton) findViewById(R.id.playButtonOriginal);
		mNoImageButton = (ImageButton) findViewById(R.id.noImageButton);
		mNoButton = (Button) findViewById(R.id.noButton);
		mYesImageButton = (ImageButton) findViewById(R.id.yesImageButton);
		mYesButton = (Button) findViewById(R.id.yesButton);
		mAudioCurrentDurationRecorded = (TextView) findViewById(R.id.audioCurrentDurationRecorded);
		mAudioTotalDurationRecorded = (TextView) findViewById(R.id.audioTotalDurationRecorded);
		mAudioCurrentDurationOriginal = (TextView) findViewById(R.id.audioCurrentDurationOriginal);
		mAudioTotalDurationOriginal = (TextView) findViewById(R.id.audioTotalDurationOriginal);

		mAudioProgressBarRecorded = (SeekBar) findViewById(R.id.audioProgressBarRecorded);
		mAudioProgressBarOriginal = (SeekBar) findViewById(R.id.audioProgressBarOriginal);
		mAudioProgressBarRecorded.setProgress(0);
		mAudioProgressBarOriginal.setMax(100);
		mAudioProgressBarRecorded.setProgress(0);
		mAudioProgressBarOriginal.setMax(100);

		// Get the location of the recorded and original file and create audio
		// players
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String recordedFileName = extras
					.getString(EXTRA_RECORDED_FILE_NAME);
			mRecordedFile = Uri.parse(recordedFileName);
			mPlayerRecorded = new AudioPlayer(mRecordedFile);

			mOriginalFile = extras.getParcelable(EXTRA_ORIGINAL_FILE_NAME);
			mPlayerOriginal = new AudioPlayer(mOriginalFile);
		}

		// Plays user's audio file (the recorded file)
		mPlayButtonRecorded.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerOriginal.pause();
				mPlayButtonOriginal.setBackgroundResource(R.drawable.play);
				mPlayerRecorded.play(SubmissionActivity.this);
				if (mPlayerRecorded.isPlaying()) {
					// Update progress bar and total time
					updateProgressBar();
					long totalDuration = mPlayerRecorded.getDuration();
					mAudioTotalDurationRecorded.setText(TimeConverter
							.milliSecondsToTimer(totalDuration));
					
					mPlayButtonRecorded.setBackgroundResource(R.drawable.pause);
				} else {
					mPlayButtonRecorded.setBackgroundResource(R.drawable.play);
				}
			}
		});

		// Plays original audio file
		mPlayButtonOriginal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerRecorded.pause();
				mPlayButtonRecorded.setBackgroundResource(R.drawable.play);
				mPlayerOriginal.play(SubmissionActivity.this);
				if (mPlayerOriginal.isPlaying()) {
					
					// Update progress bar and total time
					updateProgressBar();
					long totalDuration = mPlayerOriginal.getDuration();
					mAudioTotalDurationOriginal.setText(TimeConverter
							.milliSecondsToTimer(totalDuration));
					
					mPlayButtonOriginal.setBackgroundResource(R.drawable.pause);
				} else {
					mPlayButtonOriginal.setBackgroundResource(R.drawable.play);
				}
			}
		});

		// Press no, record again
		OnClickListener noListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopPlayers();
				finish();
			}
		};
		mNoImageButton.setOnClickListener(noListener);
		mNoButton.setOnClickListener(noListener);

		// Press yes, submit
		OnClickListener yesListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopPlayers();
				Intent i = new Intent(SubmissionActivity.this,
						SubmissionConfirmationActivity.class);
				startActivity(i);
			}
		};

		mYesImageButton.setOnClickListener(yesListener);
		mYesButton.setOnClickListener(yesListener);

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
				int totalDurationOriginal = (int) mPlayerOriginal.getDuration();
				int currentPositionOriginal = TimeConverter.progressToTimer(
						seekBar.getProgress(), totalDurationOriginal);
				
				int totalDurationRecorded = (int) mPlayerOriginal.getDuration();
				int currentPositionRecorded = TimeConverter.progressToTimer(
						seekBar.getProgress(), totalDurationRecorded);

				// Move audio players forward or backward appropriate number of
				// seconds
				mPlayerOriginal.seekTo(currentPositionOriginal);
				mPlayerRecorded.seekTo(currentPositionRecorded);

				// Update timers
				updateProgressBar();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		};
		mAudioProgressBarOriginal.setOnSeekBarChangeListener(seekBarChangeListener);
		mAudioProgressBarRecorded.setOnSeekBarChangeListener(seekBarChangeListener);
	}

	// Stops the audio players
	private void stopPlayers() {
		mPlayerRecorded.stop();
		mPlayerOriginal.stop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopPlayers();
	}

	// TODO horrible amount of redundancy ; try to find a way to factor out
	// common code

	// Update timer and audio progress bar for the original recording
	private void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long currentDurationOriginal = mPlayerOriginal.getCurrentPosition();
			long totalDurationOriginal = mPlayerOriginal.getDuration();
			
			long currentDurationRecorded = mPlayerRecorded.getCurrentPosition();
			long totalDurationRecorded = mPlayerRecorded.getDuration();

			// Update the current audio duration labels 
			mAudioCurrentDurationOriginal.setText(TimeConverter
					.milliSecondsToTimer(currentDurationOriginal));
			mAudioCurrentDurationRecorded.setText(TimeConverter
					.milliSecondsToTimer(currentDurationRecorded));

			// Update the progress bars
			int progressOriginal = TimeConverter.getProgressPercentage(currentDurationOriginal,
					totalDurationOriginal);
			int progressRecorded = TimeConverter.getProgressPercentage(currentDurationRecorded,
					totalDurationRecorded);
			mAudioProgressBarOriginal.setProgress(progressOriginal);
			mAudioProgressBarRecorded.setProgress(progressRecorded);

			// Run this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};
}
