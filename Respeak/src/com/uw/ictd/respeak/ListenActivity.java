package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dropbox.chooser.android.DbxChooser;

public class ListenActivity extends Activity {

	private TextView mRequestorName;
	private TextView mMaxRewardAmount;
	private TextView mCurrentRecording;
	private TextView mAudioCurrentDurationLabel;
	private TextView mAudioTotalDurationLabel;
	private Button mChooserButton;
	private DbxChooser mChooser;
	private AudioPlayer mPlayer = new AudioPlayer();
	private ImageButton mPlayButton;
	private Button mListenAgainButton;
	private ImageButton mListenAgainImageButton;
	private Button mRespeakButton;
	private ImageButton mRespeakImageButton;
	private SeekBar mAudioProgressBar;
	private Handler mHandler = new Handler();
	// private UpdateTimeTask mUpdateTimeTask;

	static final String EXTRA_PHONE_NUMBER = "com.uw.ictd.respeak.phone_number";
	static final int REQUEST_CODE = 0;
	static final int DBX_CHOOSER_REQUEST = 0;
	static final int REQUEST_LINK_TO_DBX = 0;
	private static final String APP_KEY = "07r2uvgq7r0446r";
	private static final String APP_SECRET = "m8gxr8wh6anshmw";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mRequestorName = (TextView) findViewById(R.id.requestorName);
		mMaxRewardAmount = (TextView) findViewById(R.id.maxRewardAmount);
		mCurrentRecording = (TextView) findViewById(R.id.currentRecordingSelected);
		mAudioCurrentDurationLabel = (TextView) findViewById(R.id.audioCurrentDurationLabel);
		mAudioTotalDurationLabel = (TextView) findViewById(R.id.audioTotalDurationLabel);

		mRequestorName.setVisibility(View.INVISIBLE);
		mMaxRewardAmount.setVisibility(View.INVISIBLE);
		mCurrentRecording.setVisibility(View.INVISIBLE);

		mChooserButton = (Button) findViewById(R.id.chooserButton);
		mPlayButton = (ImageButton) findViewById(R.id.playButton);
		mListenAgainImageButton = (ImageButton) findViewById(R.id.listenAgainImageButton);
		mListenAgainButton = (Button) findViewById(R.id.listenAgainButton);
		mRespeakImageButton = (ImageButton) findViewById(R.id.respeakImageButton);
		mRespeakButton = (Button) findViewById(R.id.respeakButton);

		mAudioProgressBar = (SeekBar) findViewById(R.id.audioProgressBar);
		mAudioProgressBar.setProgress(0);
		mAudioProgressBar.setMax(100);

		// Create a new task to update time
		// mUpdateTimeTask = new UpdateTimeTask(mPlayer,
		// mAudioCurrentDurationLabel, mAudioTotalDurationLabel,
		// mAudioProgressBar, mHandler);

		mChooser = new DbxChooser(APP_KEY);

		// Allows user to pick audio file to open from Dropbox
		mChooserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbxChooser.ResultType resultType = DbxChooser.ResultType.DIRECT_LINK;
				mChooser.forResultType(resultType).launch(ListenActivity.this,
						DBX_CHOOSER_REQUEST);
			}
		});

		// Allows user to play/pause recording
		mPlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.play(ListenActivity.this);
				if (mPlayer.isPlaying()) {
					// Update progress bar and total time
					updateProgressBar();
					long totalDuration = mPlayer.getDuration();
					mAudioTotalDurationLabel.setText(TimeConverter
							.milliSecondsToTimer(totalDuration));

					mPlayButton.setBackgroundResource(R.drawable.pause);
				} else {
					mPlayButton.setBackgroundResource(R.drawable.play);
				}
			}
		});

		// Allows user to listen to the recording again. If the recording is
		// already playing, restarts from the beginning
		OnClickListener listenAgainListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPlayer.stop();
				mPlayer.play(ListenActivity.this);
				mPlayButton.setBackgroundResource(R.drawable.pause);
			}
		};
		mListenAgainButton.setOnClickListener(listenAgainListener);
		mListenAgainImageButton.setOnClickListener(listenAgainListener);

		// Starts the recording activity
		OnClickListener respeakListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Stop playing the audio and reset the background
				mPlayer.stop();
				mPlayButton.setBackgroundResource(R.drawable.play);

				Intent i = new Intent(ListenActivity.this, RecordActivity.class);
				i.putExtra(SubmissionActivity.EXTRA_ORIGINAL_FILE_NAME,
						mPlayer.getUri());
				startActivityForResult(i, REQUEST_CODE);
			}
		};
		mRespeakButton.setOnClickListener(respeakListener);
		mRespeakImageButton.setOnClickListener(respeakListener);

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
		mAudioProgressBar.setOnSeekBarChangeListener(seekBarChangeListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DBX_CHOOSER_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				DbxChooser.Result result = new DbxChooser.Result(data);
				Log.d("main", "Link to selected file: " + result.getLink());

				// Check if the user selected an mp3 file
				if (result.getName().toString().endsWith(".mp3")
						|| result.getName().toString().endsWith(".MP3")) {
					// Update to show the requestor's name
					// TODO: possibly implement by parsing the file name
					mRequestorName.setText("Name of requestor goes here.",
							TextView.BufferType.NORMAL);
					mRequestorName.setVisibility(View.VISIBLE);

					// Update to show the maximum reward amount
					mMaxRewardAmount.setText("Max reward amount goes here.",
							TextView.BufferType.NORMAL);
					mMaxRewardAmount.setVisibility(View.VISIBLE);

					// Update to show the current recording selected
					mCurrentRecording.setText(result.getName().toString(),
							TextView.BufferType.NORMAL);

					// Pass the player the new recording to be played
					mPlayer.stop();
					Uri recording = result.getLink();
					mPlayer = new AudioPlayer(recording);

					// Error if the user does not select an audio file
				} else {
					mCurrentRecording.setText(
							"Error. You must select an audio file.",
							TextView.BufferType.NORMAL);
				}
				mCurrentRecording.setVisibility(View.VISIBLE);

			} else {
				// Failed or was cancelled by the user
				((TextView) findViewById(R.id.currentRecordingSelected))
						.setText("Failed to select recording.");
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPlayer.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			mAudioCurrentDurationLabel.setText(TimeConverter
					.milliSecondsToTimer(currentDuration));

			// Update the progress bar
			int progress = TimeConverter.getProgressPercentage(currentDuration,
					totalDuration);
			mAudioProgressBar.setProgress(progress);

			// Run this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};
}
