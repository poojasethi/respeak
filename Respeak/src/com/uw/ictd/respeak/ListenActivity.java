package com.uw.ictd.respeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dropbox.chooser.android.DbxChooser;

public class ListenActivity extends Activity {

	private Button mChooserButton;
	private DbxChooser mChooser;
	private AudioPlayer mPlayer = new AudioPlayer();
	private Button mPlayButton;
	private Button mListenAgainButton;
	private Button mRespeakButton;

	static final int DBX_CHOOSER_REQUEST = 0; // may need to change
	static final String APP_KEY = "0r2uvgq7r0446r";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);

		mChooserButton = (Button) findViewById(R.id.chooserButton);
		mPlayButton = (Button) findViewById(R.id.playButton);
		mListenAgainButton = (Button) findViewById(R.id.listenAgainButton);
		mRespeakButton = (Button) findViewById(R.id.respeakButton);

		mChooser = new DbxChooser(APP_KEY);

		mChooserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mChooser.forResultType(DbxChooser.ResultType.PREVIEW_LINK)
						.launch(ListenActivity.this, DBX_CHOOSER_REQUEST);
			}
		});

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
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == DBX_CHOOSER_REQUEST) {
//	        if (resultCode == Activity.RESULT_OK) {
//	            DbxChooser.Result result = new DbxChooser.Result(data);
//	            Log.d("main", "Link to selected file: " + result.getLink());
//
//	            // Handle the result
//	        } else {
//	            // Failed or was cancelled by the user.
//	        }
//	    } else {
//	        super.onActivityResult(requestCode, resultCode, data);
//	    }
//	}
}
