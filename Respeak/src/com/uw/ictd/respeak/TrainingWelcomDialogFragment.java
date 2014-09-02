package com.uw.ictd.respeak;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class TrainingWelcomDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.welcome)
				.setMessage(R.string.training_dialog)
				.setPositiveButton(android.R.string.ok, null).create();
	}
}
