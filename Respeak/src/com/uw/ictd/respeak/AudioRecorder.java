package com.uw.ictd.respeak;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {

	private static final String TAG = "AudioRecorder";
	private MediaRecorder mRecorder;
	private String mFileName;
	private boolean isRecording;

    protected void startRecording() {
    	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
    	mFileName += "/recorderTest.3gp";
    	
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
        isRecording = true;
    }

    protected void stopRecording() {
    	if (mRecorder != null) {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	        isRecording = false;
    	}
    }
    
    protected void pauseRecording() {
    	if (mRecorder != null) {
    		mRecorder.stop();
    		isRecording = false;
    	}
    }
    
    protected String getFileName() {
    	return mFileName;
    }
    
    protected void stop() {
    	if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
    
    protected boolean isRecording() {
    	return isRecording;
    }
    
}
