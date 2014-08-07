package com.uw.ictd.respeak;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ListenActivity extends Activity {

	private AudioPlayer mPlayer = new AudioPlayer();
	private Button mPlayButton;
	private Button mListenAgainButton;
	private Button mRespeakButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);

		mPlayButton = (Button) findViewById(R.id.playButton);
		mListenAgainButton = (Button) findViewById(R.id.listenAgainButton);
		mRespeakButton = (Button) findViewById(R.id.respeakButton);

		// Allows user to play/pause recording
		mPlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.play(ListenActivity.this);
				if (!mPlayer.isPlaying()) {
					mPlayButton.setText(R.string.play);
				} else {
					mPlayButton.setText(R.string.pause);
				}
			}
		});

		
		// Allows user to listen to the recording again. If the recording is
		// already playing, restarts from the beginning
		mListenAgainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.stop();
				mPlayer.play(ListenActivity.this);
				mPlayButton.setText(R.string.pause);
			}
		});

		// TODO: implement RecordActivity
		mRespeakButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}
}
