package com.uw.ictd.respeak;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class ListenActivity extends Activity {

	private Button mChooserButton;
	private DbxChooser mChooser;
	private DbxAccountManager mDbxAcctMgr;
	private TextView mTestOutput;
	private AudioPlayer mPlayer = new AudioPlayer();
	private Button mPlayButton;
	private Button mListenAgainButton;
	private Button mRespeakButton;
	private SeekBar mAudioProgressBar;

	static final int DBX_CHOOSER_REQUEST = 0; 
	static final int REQUEST_LINK_TO_DBX = 0;
	static final String APP_KEY = "07r2uvgq7r0446r";
	static final String APP_SECRET = "m8gxr8wh6anshmw";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		
		mTestOutput = (TextView) findViewById(R.id.currentRecordingSelected);
		mChooserButton = (Button) findViewById(R.id.chooserButton);
		mPlayButton = (Button) findViewById(R.id.playButton);
		mListenAgainButton = (Button) findViewById(R.id.listenAgainButton);
		mRespeakButton = (Button) findViewById(R.id.respeakButton);
		
		mChooserButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickLinkToDropbox(v);
			}
		});
		
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);

//		mChooser = new DbxChooser(APP_KEY);

		// Allows user to pick audio file to open from Dropbox 
//		mChooserButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				DbxChooser.ResultType resultType = DbxChooser.ResultType.FILE_CONTENT;
//				mChooser.forResultType(resultType).launch(ListenActivity.this,
//						DBX_CHOOSER_REQUEST);
//			}
//		});

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
	
	private void onClickLinkToDropbox(View view) {
	    mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            doDropboxTest();
	        } else {
	        	((TextView) findViewById(R.id.currentRecordingSelected)).setText("Linking failed");
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	private void doDropboxTest() {
        mTestOutput.setText("Dropbox Sync API Version "+DbxAccountManager.SDK_VERSION_NAME+"\n");
        try {
            final String TEST_DATA = "Hello Dropbox";
            final String TEST_FILE_NAME = "hello_dropbox.txt";
            DbxPath testPath = new DbxPath(DbxPath.ROOT, TEST_FILE_NAME);

            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

            // Print the contents of the root folder.  This will block until we can
            // sync metadata the first time.
            List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
            mTestOutput.append("\nContents of app folder:\n");
            for (DbxFileInfo info : infos) {
                mTestOutput.append("    " + info.path + ", " + info.modifiedTime + '\n');
            }

            // Create a test file only if it doesn't already exist.
            if (!dbxFs.exists(testPath)) {
                DbxFile testFile = dbxFs.create(testPath);
                try {
                    testFile.writeString(TEST_DATA);
                } finally {
                    testFile.close();
                }
                mTestOutput.append("\nCreated new file '" + testPath + "'.\n");
            }

            // Read and print the contents of test file.  Since we're not making
            // any attempt to wait for the latest version, this may print an
            // older cached version.  Use getSyncStatus() and/or a listener to
            // check for a new version.
            if (dbxFs.isFile(testPath)) {
                String resultData;
                DbxFile testFile = dbxFs.open(testPath);
                try {
                    resultData = testFile.readString();
                } finally {
                    testFile.close();
                }
                mTestOutput.append("\nRead file '" + testPath + "' and got data:\n    " + resultData);
            } else if (dbxFs.isFolder(testPath)) {
                mTestOutput.append("'" + testPath.toString() + "' is a folder.\n");
            }
        } catch (IOException e) {
            mTestOutput.setText("Dropbox test failed: " + e);
        }
    }

	// TODO: stop this from crashing :(
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == DBX_CHOOSER_REQUEST) {
//			if (resultCode == Activity.RESULT_OK) {
//				DbxChooser.Result result = new DbxChooser.Result(data);
//				Log.d("main", "Link to selected file: " + result.getLink());
//
//				// Handle the result
//				showLink(R.id.uri, result.getLink());
//				((TextView) findViewById(R.id.currentRecordingSelected))
//						.setText(result.getName().toString(),
//								TextView.BufferType.NORMAL);
//
//			} else {
//				// Failed or was cancelled by the user.
//				((TextView) findViewById(R.id.currentRecordingSelected)).setText("failed");
//			}
//		} else {
//			super.onActivityResult(requestCode, resultCode, data);
//		}
//	}
//
//	private void showLink(int id, Uri uri) {
//		TextView v = (TextView) findViewById(id);
//		if (uri == null) {
//			v.setText("", TextView.BufferType.NORMAL);
//			return;
//		}
//		v.setText(uri.toString(), TextView.BufferType.NORMAL);
//		v.setMovementMethod(LinkMovementMethod.getInstance());
//	}
}
