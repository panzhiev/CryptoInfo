package com.crypto.cryptoinfo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.background.service.NotificationService;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.AllCoinsFragment;
import com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment.FavouritesCoinsFragment;
import com.crypto.cryptoinfo.utils.AndroidUtils;

import static com.crypto.cryptoinfo.utils.Constants.EMAIL_FOR_CONTACT;
import static com.crypto.cryptoinfo.utils.Constants.ENABLE_AUTO_NIGHT_MODE;
import static com.crypto.cryptoinfo.utils.Constants.ENABLE_NIGHT_MODE;
import static com.crypto.cryptoinfo.utils.Constants.MAIN_SCREEN;
import static com.crypto.cryptoinfo.utils.Constants.PLAY_MARKET_PATH;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();
    DrawerLayout drawer;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        checkingForNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!AndroidUtils.isNotificationServiceRunning(NotificationService.class, this)) {
            Intent serviceIntent = new Intent(this, NotificationService.class);
            startService(serviceIntent);
        }

        navigateOnFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Started");

        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    private void navigateOnFragment() {
        String valueMainScreen = prefs.getString(MAIN_SCREEN, "0");
        if (valueMainScreen.equals("0")) {
            AllCoinsFragment allCoinsFragment = AllCoinsFragment.newInstance();
            navigator(allCoinsFragment, allCoinsFragment.getCurrentTag());
        } else {
            FavouritesCoinsFragment favouritesCoinsFragment = FavouritesCoinsFragment.newInstance();
            navigator(favouritesCoinsFragment, favouritesCoinsFragment.getCurrentTag());
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        IBaseFragment iBaseFragment = null;
        for (Fragment f : fragmentManager.getFragments()) {
            if (f instanceof IBaseFragment) {
                iBaseFragment = (IBaseFragment) f;
                break;
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (iBaseFragment != null) {
            iBaseFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void onCloseNavigationDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.all_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourites) {

            onCloseNavigationDrawer();
            new Thread(() -> {
                try {
                    Thread.sleep(250);
                    FavouritesCoinsFragment favouritesCoinsFragment = FavouritesCoinsFragment.newInstance();
                    navigator(favouritesCoinsFragment, favouritesCoinsFragment.getCurrentTag());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (id == R.id.nav_all_coins) {

            onCloseNavigationDrawer();
            new Thread(() -> {
                try {
                    Thread.sleep(250);
                    AllCoinsFragment allCoinsFragment = AllCoinsFragment.newInstance();
                    navigator(allCoinsFragment, allCoinsFragment.getCurrentTag());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (id == R.id.nav_feed) {

        } else if (id == R.id.nav_convert) {

        } else if (id == R.id.nav_share) {
            AndroidUtils.share(this, PLAY_MARKET_PATH + getPackageName());
        } else if (id == R.id.nav_connect) {
            AndroidUtils.sendEmail(this, EMAIL_FOR_CONTACT, "");
        } else if (id == R.id.nav_settings) {
            onCloseNavigationDrawer();
            new Thread(() -> {
                try {
                    Thread.sleep(250);
                    startActivity(new Intent(this, SettingsActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return true;
    }


    public void navigator(Fragment fragment, String TAG) {

        Fragment f = fragmentManager.findFragmentByTag(TAG);

        if (!(f != null && f.isVisible())) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container_content_main, fragment, TAG)
                    .commit();
        }
    }

    public void navigatorBackPressed(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.container_content_main, fragment, ((IBaseFragment) fragment).getCurrentTag())
                .commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {
            case ENABLE_NIGHT_MODE:
                boolean b = sharedPreferences.getBoolean(key, false);
                AppCompatDelegate.setDefaultNightMode(b ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
            case ENABLE_AUTO_NIGHT_MODE:
                boolean b1 = sharedPreferences.getBoolean(key, false);
                AppCompatDelegate.setDefaultNightMode(b1 ? AppCompatDelegate.MODE_NIGHT_AUTO : AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prefs != null)
            prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void checkingForNightMode() {

        boolean isNightModeEnabled = prefs.getBoolean(ENABLE_NIGHT_MODE, false);
        boolean isAutoNightModeEnabled = prefs.getBoolean(ENABLE_AUTO_NIGHT_MODE, false);

        if (isNightModeEnabled) {
            if (isAutoNightModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
