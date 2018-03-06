package com.crypto.cryptoinfo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.ui.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private int mThemeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final AppCompatDelegate delegate = getDelegate();
        delegate.installViewFactory();
        delegate.onCreate(savedInstanceState);
        if (delegate.applyDayNight() && mThemeId != 0) {
            // If DayNight has been applied, we need to re-apply the theme for
            // the changes to take effect. On API 23+, we should bypass
            // setTheme(), which will no-op if the theme ID is identical to the
            // current theme ID.
            if (Build.VERSION.SDK_INT >= 23) {
                onApplyThemeResource(getTheme(), mThemeId, false);
            } else {
                setTheme(mThemeId);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);

        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        getFragmentManager().beginTransaction().replace(R.id.settings_container, SettingsFragment.newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                onBackPressed();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTheme(@StyleRes final int resid) {
        super.setTheme(resid);
        // Keep hold of the theme id so that we can re-set it later if needed
        mThemeId = resid;
    }
}
