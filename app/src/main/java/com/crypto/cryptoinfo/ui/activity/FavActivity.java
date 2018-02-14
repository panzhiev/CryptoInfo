package com.crypto.cryptoinfo.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter.CoinsAdapter;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.viewModel.CoinsListViewModel;
import com.crypto.cryptoinfo.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavActivity extends AppCompatActivity implements CoinsAdapter.OnCoinItemClickListener{

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.rv_fav_settings)
    public RecyclerView mRvCoins;

    private CoinsAdapter mCoinsAdapter;
    private CoinsListViewModel mCoinsListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar_fav);
        setSupportActionBar(toolbar);
        setUpRecyclerView();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        mCoinsListViewModel = ViewModelProviders.of(this).get(CoinsListViewModel.class);
        mCoinsListViewModel.getCoinsList().observe(this, coins -> {
                    setList((ArrayList) coins);
                }
        );
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setList(ArrayList list) {
        Log.d(TAG, "setList started");
        if (mCoinsAdapter == null) {
            Log.d(TAG, "mCoinsAdapter == null");
            mCoinsAdapter = new CoinsAdapter(list, Constants.COIN_FAV_SETTINGS_VIEW_TYPE, this);
            mRvCoins.setAdapter(mCoinsAdapter);
        } else {
            Log.d(TAG, "mCoinsAdapter != null");
            mCoinsAdapter.reloadList(list);
        }
    }

    private void setUpRecyclerView() {
        mRvCoins.setLayoutManager(new LinearLayoutManager(this));
        mRvCoins.setHasFixedSize(true);
    }

    @Override
    public void onCoinItemClick(CoinPojo coinPojo) {

    }
}
