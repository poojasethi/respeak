package com.uw.ictd.respeak;

import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

public class UpdateTimeTask implements Runnable{
	private AudioPlayer mPlayer;
	private TextView mAudioCurrentDurationLabel;
	private TextView mAudioTotalDurationLabel;
	private SeekBar mAudioProgressBar;
	private Handler mHandler;
	
	public UpdateTimeTask(AudioPlayer player, TextView currentDuration, 
			TextView totalDuration, SeekBar audioProgressBar, Handler handler){
		mPlayer = player;
		mAudioCurrentDurationLabel = currentDuration;
		mAudioTotalDurationLabel = totalDuration;
		mAudioProgressBar = audioProgressBar;
		mHandler = handler;
	}

	@Override
	public void run() {
		long currentDuration = mPlayer.getCurrentPosition();
		long totalDuration = mPlayer.getDuration();

		// Update the timer labels
		mAudioCurrentDurationLabel.setText(TimeConverter
				.milliSecondsToTimer(currentDuration));
		mAudioTotalDurationLabel.setText(TimeConverter
				.milliSecondsToTimer(totalDuration));

		// Update the progress bar
		int progress = TimeConverter.getProgressPercentage(currentDuration,
				totalDuration);
		mAudioProgressBar.setProgress(progress);

		// Run this thread after 100 milliseconds
		mHandler.postDelayed(this, 100);
	}
	
	public void updateProgressBar() {
		mHandler.postDelayed(this, 100);
	}
}
