package com.crypto.cryptoinfo.ui.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;

import com.crypto.cryptoinfo.R;

public class SettingsFragment extends PreferenceFragment {

    @NonNull
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_screen);
    }
}
