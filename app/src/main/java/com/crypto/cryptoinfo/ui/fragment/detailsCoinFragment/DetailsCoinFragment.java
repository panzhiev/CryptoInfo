package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.adapter.ExchangesAdapter;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsCoinFragment extends Fragment implements IBaseFragment {

    @BindView(R.id.iv_coin_icon)
    ImageView mIvCoinIcon;
    @BindView(R.id.iv_coin_icon_exchanges)
    ImageView mIvCoinIconExchanges;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_price_value)
    TextView mTvPriceValue;
    @BindView(R.id.tv_market_cap)
    TextView mTvMarketCap;
    @BindView(R.id.tv_market_cap_value)
    TextView mTvMarketCapValue;
    @BindView(R.id.tv_available_supply)
    TextView mTvAvailableSupply;
    @BindView(R.id.tv_available_supply_value)
    TextView mTvAvailableSupplyValue;
    @BindView(R.id.tv_max_supply)
    TextView mTvMaxSupply;
    @BindView(R.id.tv_max_supply_value)
    TextView mTvMaxSupplyValue;
    @BindView(R.id.tv_24h_volume)
    TextView mTvVolume24h;
    @BindView(R.id.tv_24h_volume_value)
    TextView mTvVolume24hValue;
    @BindView(R.id.tv_percent_change_1h)
    TextView mTvPercentChange1h;
    @BindView(R.id.tv_percent_change_1h_value)
    TextView mTvPercentChange1hValue;
    @BindView(R.id.tv_percent_change_24h)
    TextView mTvPercentChange24h;
    @BindView(R.id.tv_percent_change_24h_value)
    TextView mTvPercentChange24hValue;
    @BindView(R.id.tv_percent_change_7d)
    TextView mTvPercentChange7d;
    @BindView(R.id.tv_percent_change_7d_value)
    TextView mTvPercentChange7dValue;
    @BindView(R.id.rv_markets)
    RecyclerView mRvExchanges;
    @BindView(R.id.loading_indicator_markets)
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    @BindView(R.id.tv_no_exchanges)
    TextView mTvNoExchanges;

    private final String TAG = getClass().getSimpleName();

    private CoinsPresenter mCoinsPresenter;

    private CoinPojo mCoinPojo;
    private ExchangesAdapter mExchangesAdapter;

    public DetailsCoinFragment() {
        // Required empty public constructor
    }

    public static DetailsCoinFragment newInstance() {
        return new DetailsCoinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCoinsPresenter = new CoinsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_details_coin, container, false);
        ButterKnife.bind(this, view);

        setUpRecyclerView();
        setListeners();

        mCoinPojo = ((CoinInfoActivity) getActivity()).getCoinPojo();
        parseCoinInfo();

        mCoinsPresenter.getCoinSnapshot(mCoinPojo.getSymbol(), "USD");

        return view;
    }

    private void setUpRecyclerView() {
        mRvExchanges.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvExchanges.setHasFixedSize(true);
        mRvExchanges.setNestedScrollingEnabled(false);
    }

    private String formattingDoubleValues(String rawValue) {
        return getString(R.string.usd_symbol) + " " + String.format(Locale.getDefault(), "%1$,.2f",
                Double.parseDouble(rawValue));
    }

    private void setListeners() {
    }

    @Override
    public void setList(ArrayList list) {
        Log.d(TAG, "setList started");

        if (list == null || list.isEmpty()) {
            Log.d(TAG, "list null or empty");
            mTvNoExchanges.setVisibility(View.VISIBLE);
            return;
        } else {
            mTvNoExchanges.setVisibility(View.GONE);
        }

        Log.d(TAG, list.toString());

        if (mExchangesAdapter == null) {
            Log.d(TAG, "mExchangesAdapter == null");
            mExchangesAdapter = new ExchangesAdapter(list);
            mRvExchanges.setAdapter(mExchangesAdapter);
        } else {
            Log.d(TAG, "mCoinsAdapter != null");
            mExchangesAdapter.reloadList(list);
        }
    }

    @Override
    public void reloadList(ArrayList list) {

    }

    @Override
    public void showError() {
        Log.d(TAG, "showError");
        DialogFactory.createGenericErrorDialog(getContext(), R.string.error_message).show();
    }

    @Override
    public void onBackPressed() {

    }

    public String getCurrentTag() {
        return TAG;
    }

    @Override
    public void showProgressIndicator() {

        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        mAVLoadingIndicatorView.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCoinsPresenter != null) {
            mCoinsPresenter.unsubscribe();
        }
    }

    public void parseCoinInfo() {

        String currentCurrency = this.getString(R.string.usd_symbol);

        Bitmap bitmap = Utils.getBitmapFromCryptoIconsAssets(getContext(), mCoinPojo.getSymbol().toLowerCase());
        if (bitmap != null) {
            mIvCoinIcon.setImageBitmap(bitmap);
            mIvCoinIconExchanges.setImageBitmap(bitmap);
        } else {
            Glide.with(this)
                    .load(Utils.getCoinImageUrl(mCoinPojo.getNumId()))
                    .apply(new RequestOptions()
                            .centerCrop()
                            .error(Utils.getTextDrawable(getContext(), mCoinPojo.getSymbol()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(mIvCoinIcon);
            Glide.with(this)
                    .load(Utils.getCoinImageUrl(mCoinPojo.getNumId()))
                    .apply(new RequestOptions()
                            .centerCrop()
                            .error(Utils.getTextDrawable(getContext(), mCoinPojo.getSymbol()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(mIvCoinIconExchanges);
        }

        mTvPriceValue.setText(
                Utils.formatPrice(mCoinPojo.getPriceUsd())
                        .concat(currentCurrency));

        mTvMarketCapValue.setText(
                Utils.formatPrice(mCoinPojo.getMarketCapUsd())
                        .concat(currentCurrency));

        mTvAvailableSupplyValue.setText(
                Utils.formatPrice(mCoinPojo.getAvailableSupply())
                        .concat(currentCurrency));

        mTvMaxSupplyValue.setText(
                Utils.formatPrice(mCoinPojo.getMaxSupply())
                        .concat(currentCurrency));

        mTvVolume24hValue.setText(
                Utils.formatPrice(mCoinPojo.get24hVolumeUsd())
                        .concat(currentCurrency));

        Utils.formatPercentChange(getContext(), mTvPercentChange1hValue, mCoinPojo.getPercentChange1h());
        Utils.formatPercentChange(getContext(), mTvPercentChange24hValue, mCoinPojo.getPercentChange24h());
        Utils.formatPercentChange(getContext(), mTvPercentChange7dValue, mCoinPojo.getPercentChange7d());
    }
}
