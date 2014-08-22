package com.uw.ictd.respeak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class TrainingActivity extends ActionBarActivity {
	private TextView mTrainingText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);
		
		mTrainingText = (TextView) findViewById(R.id.trainingText);
		mTrainingText.setText(readText());
		mTrainingText.setMovementMethod(new ScrollingMovementMethod());
	}
	
	// Reads from a training text file and converts into a string 
	private String readText(){
		InputStream inputStream = getResources().openRawResource(R.raw.training_text);
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
}
