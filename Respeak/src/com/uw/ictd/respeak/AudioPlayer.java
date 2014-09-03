package com.uw.ictd.respeak;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class AudioPlayer {
	
	private MediaPlayer mPlayer;
	private int DEFAULT_RECORDING = R.raw.gaetano_lecture; // TODO: change default later
	
	// Recording chosen by user to be played
	private Uri mRecording;
	
	protected AudioPlayer() {
	}
	
	protected AudioPlayer(Uri uri) {
		mRecording = uri;
	}
	
	public void stop() {
		if (mPlayer != null) {
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}
	
	public void play(Context c) {
		if (mPlayer == null) {
			
			// Checks if a recording has been chosen by the user and creates the appropriate player
			if (mRecording == null) {
				mPlayer = MediaPlayer.create(c, DEFAULT_RECORDING);
			} else {
				mPlayer = MediaPlayer.create(c, mRecording);
			}
			
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					stop();	
				}
			});
			mPlayer.start();
			
		} else if (mPlayer.isPlaying()) {
			mPlayer.pause();
		} else {
			mPlayer.start();
		}
	}
	
	public boolean isPlaying() {
		if (mPlayer == null) {
			return false;
		} else {
			return mPlayer.isPlaying();
		}
	}
	
	public Uri getUri() {
		return mRecording;
	}
	
	public long getDuration() {
		if (mPlayer != null) {
			return mPlayer.getDuration();
		} else {
			return -1;
		}
	}
	
	public long getCurrentPosition() {
		if (mPlayer != null) {
			return mPlayer.getCurrentPosition();
		} else {
			return -1;
		}
	}
	
	public void seekTo(int currentPosition) {
		if (mPlayer != null) {
			mPlayer.seekTo(currentPosition);
		}
	}
}
