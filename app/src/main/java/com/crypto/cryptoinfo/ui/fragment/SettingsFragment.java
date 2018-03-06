package com.crypto.cryptoinfo.ui.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.utils.Constants;

public class SettingsFragment extends PreferenceFragment {

    private SwitchPreference mSwitchPreferenceNightMode;
    private SwitchPreference mSwitchPreferenceAutoNightMode;

    @NonNull
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        addPreferencesFromResource(R.xml.pref_screen);

        mSwitchPreferenceNightMode = (SwitchPreference) findPreference(Constants.ENABLE_NIGHT_MODE);
        mSwitchPreferenceAutoNightMode = (SwitchPreference) findPreference(Constants.ENABLE_AUTO_NIGHT_MODE);

        mSwitchPreferenceNightMode.setOnPreferenceChangeListener((preference, o) -> {
            boolean isNightMode = (boolean) o;
            if (isNightMode){
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
    }

    private void recreateActivity() {
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}