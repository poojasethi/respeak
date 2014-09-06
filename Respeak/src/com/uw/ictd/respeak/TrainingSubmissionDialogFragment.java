package com.uw.ictd.respeak;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class TrainingSubmissionDialogFragment extends DialogFragment {
	private Context mContext;
	
	public TrainingSubmissionDialogFragment(Context c) {
		mContext = c;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.are_you_sure)
				.setMessage(R.string.training_dialog_submission)
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(mContext, ListenActivity.class);
						startActivity(i);
					}
				}).create();
				
	}
}
