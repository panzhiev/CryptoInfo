package com.crypto.cryptoinfo.ui.fragment;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.utils.Constants;

public class SettingsFragment extends PreferenceFragment{

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private SwitchPreference mSwitchPreferenceNightMode;
    private SwitchPreference mSwitchPreferenceAutoNightMode;
    private ListPreference mListPreferenceMainScreen;

    @NonNull
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_screen);

        mSwitchPreferenceNightMode = (SwitchPreference) findPreference(Constants.ENABLE_NIGHT_MODE);
        mSwitchPreferenceAutoNightMode = (SwitchPreference) findPreference(Constants.ENABLE_AUTO_NIGHT_MODE);
        mListPreferenceMainScreen = (ListPreference) findPreference(Constants.MAIN_SCREEN);
        setPreferenceListeners();

        setPreferenceSummary(mListPreferenceMainScreen, mListPreferenceMainScreen.getValue());
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index == 0) {
                listPreference.setSummary(getResources().getString(R.string.title_all_coins));
            } else {
                listPreference.setSummary(getResources().getString(R.string.title_fav_coins));
            }
        }
    }

    private void recreateActivity() {
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setPreferenceListeners() {
        mSwitchPreferenceNightMode.setOnPreferenceChangeListener((preference, o) -> {
            boolean isNightMode = (boolean) o;
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mSwitchPreferenceAutoNightMode.setChecked(false);
            }

            recreateActivity();
            return true;
        });

        mSwitchPreferenceAutoNightMode.setOnPreferenceChangeListener((preference, o) -> {
            boolean isAutoNightMode = (boolean) o;
            AppCompatDelegate.setDefaultNightMode(isAutoNightMode ? AppCompatDelegate.MODE_NIGHT_AUTO : AppCompatDelegate.MODE_NIGHT_YES);
            recreateActivity();
            return true;
        });

        mListPreferenceMainScreen.setOnPreferenceChangeListener((preference, o) -> {
           String value  = (String) o;
           setPreferenceSummary(preference, value);
           return true;
        });
    }
}