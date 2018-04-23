package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.adapter.ExchangesAdapter;
import com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.viewModel.MarketsViewModel;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crypto.cryptoinfo.utils.Constants.BTC;
import static com.crypto.cryptoinfo.utils.Constants.BTC_SYMBOL;
import static com.crypto.cryptoinfo.utils.Constants.EUR;
import static com.crypto.cryptoinfo.utils.Constants.EUR_SYMBOL;
import static com.crypto.cryptoinfo.utils.Constants.TIME_TO_UPD;
import static com.crypto.cryptoinfo.utils.Constants.USD;
import static com.crypto.cryptoinfo.utils.Constants.USD_SYMBOL;

public class DetailsCoinFragment extends Fragment implements IBaseFragment {

    @BindView(R.id.iv_coin_icon)
    ImageView mIvCoinIcon;
    @BindView(R.id.iv_coin_icon_markets)
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
    @BindView(R.id.tv_last_upd_markets)
    TextView mTvLastUpdMarkets;
    @BindView(R.id.ib_refresh)
    ImageButton mIbRefresh;

    private MarketsViewModel mMarketsViewModel;

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
        if (mCoinsPresenter == null) {
            mCoinsPresenter = new CoinsPresenter(this);
        }
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

        MarketsViewModel.pair = (mCoinPojo.getSymbol() + SharedPreferencesHelper.getInstance().getCurrentCurrency()).toLowerCase();
        mMarketsViewModel = ViewModelProviders.of(this).get(MarketsViewModel.class);
        mMarketsViewModel.getMarketsList().observe(this, coins -> {
                    setList((ArrayList) coins);
                    mTvLastUpdMarkets.setText(Utils.longToDateTime(Long.parseLong(SharedPreferencesHelper.getInstance().getLastUpdMarkets())));
                }
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String lastUpd = SharedPreferencesHelper.getInstance().getLastUpdMarkets();
        mTvLastUpdMarkets.setText(Utils.longToDateTime(Long.parseLong(lastUpd)));
        if (System.currentTimeMillis() - Long.parseLong(lastUpd) > TIME_TO_UPD) {
            mCoinsPresenter.getMarketsPrices();
        }
    }

    private void setUpRecyclerView() {
        mRvExchanges.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvExchanges.setHasFixedSize(true);
        mRvExchanges.setNestedScrollingEnabled(false);
    }

    private void setListeners() {
        mIbRefresh.setOnClickListener(v -> mCoinsPresenter.getMarketsPrices());
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

        if (mExchangesAdapter == null) {
            Log.d(TAG, "mExchangesAdapter == null");
            mExchangesAdapter = new ExchangesAdapter(list);
            mRvExchanges.setAdapter(mExchangesAdapter);
        } else {
            Log.d(TAG, "mExchangesAdapter != null");
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
    public void notifyForChanges() {

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
        mIbRefresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        mAVLoadingIndicatorView.setVisibility(View.GONE);
        mIbRefresh.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCoinsPresenter != null) {
            mCoinsPresenter.unsubscribe();
        }
    }

    public void parseCoinInfo() {

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

        String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();

        String formatPrice = "";
        String formatMarketCap = "";
        String formatAvailableSupply = "";
        String formatMaxSupply = "";
        String formatVolume = "";

        switch (currentCurrency) {
            case USD:
                formatPrice = Utils.formatPrice(mCoinPojo.getPriceUsd())
                        .concat(USD_SYMBOL);
                formatMarketCap = Utils.formatMarketCap(mCoinPojo.getMarketCapUsd())
                        .concat(USD_SYMBOL);
                formatAvailableSupply = Utils.formatPrice(mCoinPojo.getAvailableSupply())
                        .concat(USD_SYMBOL);
                formatMaxSupply = Utils.formatPrice(mCoinPojo.getMaxSupply())
                        .concat(USD_SYMBOL);
                formatVolume = Utils.formatPrice(mCoinPojo.get24hVolumeUsd())
                        .concat(USD_SYMBOL);
                break;
            case EUR:
                formatPrice = Utils.formatPrice(mCoinPojo.getPriceEur())
                        .concat(EUR_SYMBOL);
                formatMarketCap = Utils.formatMarketCap(mCoinPojo.getMarketCapEur())
                        .concat(EUR_SYMBOL);

                if (mCoinPojo.getPriceUsd() == null) {
                    formatAvailableSupply = "? ".concat(EUR_SYMBOL);
                    formatMaxSupply = "? ".concat(EUR_SYMBOL);
                    formatVolume = "? ".concat(EUR_SYMBOL);
                    break;
                }

                double coefficientUsdEur = Double.parseDouble(mCoinPojo.getPriceUsd())
                        / Double.parseDouble(mCoinPojo.getPriceEur());

                if (mCoinPojo.getAvailableSupply() != null) {
                    double avSupply = Double.parseDouble(mCoinPojo.getAvailableSupply());
                    formatAvailableSupply = Utils.formatPrice(String.valueOf(avSupply / coefficientUsdEur))
                            .concat(EUR_SYMBOL);
                } else {
                    formatAvailableSupply = "? ".concat(EUR_SYMBOL);
                }

                if (mCoinPojo.getMaxSupply() != null) {
                    double maxSupply = Double.parseDouble(mCoinPojo.getMaxSupply());
                    formatMaxSupply = Utils.formatPrice(String.valueOf(maxSupply / coefficientUsdEur))
                            .concat(EUR_SYMBOL);
                } else {
                    formatMaxSupply = "? ".concat(EUR_SYMBOL);
                }

                formatVolume = Utils.formatMarketCap(mCoinPojo.get_24hVolumeEur())
                        .concat(EUR_SYMBOL);

                break;
            case BTC:
                formatPrice = Utils.formatPrice(mCoinPojo.getPriceBtc())
                        .concat(BTC_SYMBOL);

                String priceUsd = mCoinPojo.getPriceUsd();
                String marketCapUsd = mCoinPojo.getMarketCapUsd();
                String priceBtc = mCoinPojo.getPriceBtc();

                formatMarketCap = Utils.formatMarketCapForBtc(priceUsd, priceBtc, marketCapUsd)
                        .concat(BTC_SYMBOL);

                if (mCoinPojo.getPriceUsd() == null) {
                    formatAvailableSupply = "? ".concat(BTC_SYMBOL);
                    formatMaxSupply = "? ".concat(BTC_SYMBOL);
                    formatVolume = "? ".concat(BTC_SYMBOL);
                    break;
                }

                double coefficientUsdBtc = Double.parseDouble(mCoinPojo.getPriceUsd())
                        / Double.parseDouble(mCoinPojo.getPriceBtc());

                if (mCoinPojo.getAvailableSupply() != null) {
                    double avSupply = Double.parseDouble(mCoinPojo.getAvailableSupply());
                    formatAvailableSupply = Utils.formatPrice(String.valueOf(avSupply / coefficientUsdBtc))
                            .concat(BTC_SYMBOL);
                } else {
                    formatAvailableSupply = "? ".concat(BTC_SYMBOL);
                }

                if (mCoinPojo.getMaxSupply() != null) {
                    double maxSupply = Double.parseDouble(mCoinPojo.getMaxSupply());
                    formatMaxSupply = Utils.formatPrice(String.valueOf(maxSupply / coefficientUsdBtc))
                            .concat(BTC_SYMBOL);
                } else {
                    formatMaxSupply = "? ".concat(BTC_SYMBOL);
                }

                if (mCoinPojo.get24hVolumeUsd() != null) {
                    double volume = Double.parseDouble(mCoinPojo.get24hVolumeUsd());
                    formatVolume = Utils.formatPrice(String.valueOf(volume / coefficientUsdBtc))
                            .concat(BTC_SYMBOL);
                } else {
                    formatVolume = "? ".concat(BTC_SYMBOL);
                }
                break;
            default:
                break;
        }

        mTvPriceValue.setText(formatPrice);
        mTvMarketCapValue.setText(formatMarketCap);
        mTvAvailableSupplyValue.setText(formatAvailableSupply);
        mTvMaxSupplyValue.setText(formatMaxSupply);
        mTvVolume24hValue.setText(formatVolume);

        Utils.formatPercentChange(getContext(), mTvPercentChange1hValue, mCoinPojo.getPercentChange1h());
        Utils.formatPercentChange(getContext(), mTvPercentChange24hValue, mCoinPojo.getPercentChange24h());
        Utils.formatPercentChange(getContext(), mTvPercentChange7dValue, mCoinPojo.getPercentChange7d());
    }
}
