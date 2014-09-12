package com.uw.ictd.respeak;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.BaseAdapter;

public class SettingsFragment extends PreferenceFragment {

	public static final String KEY_PREF_PHONE_NUMBER = "phone_number";
	private SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		
		// Display the saved phone number
		mSettings = getPreferenceScreen().getSharedPreferences();
		Preference phoneNumberPref = findPreference(KEY_PREF_PHONE_NUMBER);
		phoneNumberPref.setSummary(mSettings.getString(KEY_PREF_PHONE_NUMBER, null));
	}

	@Override
	public void onStart() {
		super.onStart();
		
		SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				
				if (key.equals(KEY_PREF_PHONE_NUMBER)) {
					Preference selectedPref = findPreference(key);
					selectedPref.setSummary(sharedPreferences.getString(key, ""));
					((BaseAdapter) getPreferenceScreen().getRootAdapter())
							.notifyDataSetChanged();
				}
			}
		};
		mSettings.registerOnSharedPreferenceChangeListener(listener);
	}
}
