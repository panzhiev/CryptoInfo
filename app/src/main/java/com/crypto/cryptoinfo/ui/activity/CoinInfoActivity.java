package com.crypto.cryptoinfo.ui.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.fragment.chartsCoinFragment.ChartsCoinFragment;
import com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.DetailsCoinFragment;
import com.crypto.cryptoinfo.ui.fragment.notificationsFragment.NotificationsCoinFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crypto.cryptoinfo.utils.Constants.COIN;

public class CoinInfoActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private FragmentManager fm = getSupportFragmentManager();

    @BindView(R.id.tab_layout_coin_info)
    public TabLayout tabLayout;
    private CoinPojo coinPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar_coin_info);
        setSupportActionBar(toolbar);

        coinPojo = getIntent().getParcelableExtra(COIN);

        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(coinPojo.getName() + " (" + coinPojo.getSymbol() + ")");
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        setupTabLayout();
        setupTabLayoutListener();
        setCurrentTabFragment(0);
    }

    public CoinPojo getCoinPojo() {
        return coinPojo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.ic_info_outline_black_24dp)), true);
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.ic_insert_chart_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.ic_notifications_active_black_24dp)));

        try {
            tabLayout.getTabAt(0).getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorTextDefault), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void setupTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
                tab.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorTextDefault), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                assert tab != null;
                if (!tab.isSelected()) {
                    tab.select();
                }
                DetailsCoinFragment detailsCoinFragment = DetailsCoinFragment.newInstance();
                navigator(detailsCoinFragment, detailsCoinFragment.getCurrentTag());
                break;
            case 1:
                TabLayout.Tab tab1 = tabLayout.getTabAt(1);
                assert tab1 != null;
                if (!tab1.isSelected()) {
                    tab1.select();
                }
                ChartsCoinFragment chartsCoinFragment = ChartsCoinFragment.newInstance();
                navigator(chartsCoinFragment, chartsCoinFragment.getCurrentTag());
                break;
            case 2:
                TabLayout.Tab tab2 = tabLayout.getTabAt(2);
                assert tab2 != null;
                if (!tab2.isSelected()) {
                    tab2.select();
                }
                NotificationsCoinFragment notificationsCoinFragment = NotificationsCoinFragment.newInstance();
                navigator(notificationsCoinFragment, notificationsCoinFragment.getCurrentTag());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container_coin_info, fragment, fragment.getTag())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void navigator(Fragment fragment, String TAG) {

        Fragment f = fm.findFragmentByTag(TAG);

        if (!(f != null && f.isVisible())) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction
                    .replace(R.id.frame_container_coin_info, fragment, TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }
}
