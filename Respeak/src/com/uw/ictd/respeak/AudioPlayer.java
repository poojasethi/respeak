package com.uw.ictd.respeak;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
	
	private MediaPlayer mPlayer;
	
	public void stop() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void play(Context c) {
		if (mPlayer == null) {
			stop();
			
			mPlayer = MediaPlayer.create(c, R.raw.ted_talk);
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
		return mPlayer.isPlaying();
	}
}
