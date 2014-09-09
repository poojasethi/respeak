package com.uw.ictd.respeak;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.BaseAdapter;

public class SettingsFragment extends PreferenceFragment {

	public static final String KEY_PREF_PHONE_NUMBER = "phone_number";
	public static final String KEY_PREF_FULL_NAME = "full_name";
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onStart() {
		super.onStart();
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals(KEY_PREF_PHONE_NUMBER) || key.equals(KEY_PREF_FULL_NAME)) {
					Preference selectedPref = findPreference(key);
					selectedPref.setSummary(sharedPreferences.getString(key, ""));
					((BaseAdapter) getPreferenceScreen().getRootAdapter())
							.notifyDataSetChanged();
				}
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

}
