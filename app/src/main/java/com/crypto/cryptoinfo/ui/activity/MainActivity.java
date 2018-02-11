package com.crypto.cryptoinfo.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.AllCoinsFragment;
import com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment.FavouritesCoinsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getSupportFragmentManager();
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AllCoinsFragment allCoinsFragment = AllCoinsFragment.newInstance();
        navigator(allCoinsFragment, allCoinsFragment.getCurrentTag());
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

        } else if (id == R.id.nav_connect) {

        } else if (id == R.id.nav_settings) {

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
}
