package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsCoinFragment extends Fragment implements IBaseFragment {

    @BindView(R.id.iv_coin_icon)
    ImageView mIvCoinIcon;

    @BindView(R.id.tv_coin_global_info)
    TextView mTvCoinNameSymbol;

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


    private final String TAG = getClass().getSimpleName();
    private CoinPojo mCoinPojo;

    public DetailsCoinFragment() {
        // Required empty public constructor
    }

    public static DetailsCoinFragment newInstance() {
        return new DetailsCoinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_details_coin, container, false);
        ButterKnife.bind(this, view);
        setListeners();

        mCoinPojo = ((CoinInfoActivity) getActivity()).getCoinPojo();

        Bitmap bitmap = Utils.getBitmapFromCryptoIconsAssets(getContext(), mCoinPojo.getSymbol().toLowerCase());
        if (bitmap == null) {
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .fontSize(35)
                    .endConfig()
                    .buildRoundRect(mCoinPojo.getSymbol().length() <= 3 ? mCoinPojo.getSymbol() : mCoinPojo.getSymbol().substring(0, 3), Color.LTGRAY, 64);
            mIvCoinIcon.setImageDrawable(drawable);
        } else {
            mIvCoinIcon.setImageBitmap(bitmap);
        }

        mTvCoinNameSymbol.setText(mCoinPojo.getName() + " (" + mCoinPojo.getSymbol() + ") " + getString(R.string.global_info));

        String price = mCoinPojo.getPriceUsd();
        String formattingPrice;
        if (price != null && !price.isEmpty()) {
            formattingPrice = formattingDoubleValues(price);
        } else {
            formattingPrice = "";
        }
        mTvPriceValue.setText(formattingPrice);

        String marketCap = mCoinPojo.getMarketCapUsd();
        String formattingMarketCap;
        if (marketCap != null && !marketCap.isEmpty()) {
            formattingMarketCap = formattingDoubleValues(marketCap);
        } else {
            formattingMarketCap = "-";
        }
        mTvMarketCapValue.setText(formattingMarketCap);

        String availableSupply = mCoinPojo.getAvailableSupply();
        String formattingAvailableSupply;
        if (availableSupply != null && !availableSupply.isEmpty()) {
            formattingAvailableSupply = formattingDoubleValues(availableSupply);
        } else {
            formattingAvailableSupply = "-";
        }
        mTvAvailableSupplyValue.setText(formattingAvailableSupply);

        String maxSupply = mCoinPojo.getMaxSupply();
        String formattingMaxSupply;
        if (maxSupply != null && !maxSupply.isEmpty()) {
            formattingMaxSupply = formattingDoubleValues(maxSupply);
        } else {
            formattingMaxSupply = "-";
        }
        mTvMaxSupplyValue.setText(formattingMaxSupply);

        String volume24h = mCoinPojo.get24hVolumeUsd();
        String formattingVolume24h;
        if (volume24h != null && !volume24h.isEmpty()) {
            formattingVolume24h = formattingDoubleValues(volume24h);
        } else {
            formattingVolume24h = "-";
        }
        mTvVolume24hValue.setText(formattingVolume24h);

        String percent1h = mCoinPojo.getPercentChange1h();
        if (percent1h == null) {
            mTvPercentChange1hValue.setText("-");
        } else if (percent1h.contains("-")){
            mTvPercentChange1hValue.setText(percent1h + "%" + getString(R.string.arrow_down));
            mTvPercentChange1hValue.setTextColor(getResources().getColor(R.color.red));
        } else {
            mTvPercentChange1hValue.setText("+" + percent1h + "%" + getString(R.string.arrow_up));
            mTvPercentChange1hValue.setTextColor(getResources().getColor(R.color.green));
        }

        String percent24h = mCoinPojo.getPercentChange24h();
        if (percent24h == null) {
            mTvPercentChange24hValue.setText("-");
        } else if (percent24h.contains("-")){
            mTvPercentChange24hValue.setText(percent24h + "%" + getString(R.string.arrow_down));
            mTvPercentChange24hValue.setTextColor(getResources().getColor(R.color.red));
        } else {
            mTvPercentChange24hValue.setText("+" + percent24h + "%" + getString(R.string.arrow_up));
            mTvPercentChange24hValue.setTextColor(getResources().getColor(R.color.green));
        }

        String percent7d = mCoinPojo.getPercentChange7d();
        if (percent7d == null) {
            mTvPercentChange7dValue.setText("-");
        } else if (percent7d.contains("-")){
            mTvPercentChange7dValue.setText(percent7d + "%" + getString(R.string.arrow_down));
            mTvPercentChange7dValue.setTextColor(getResources().getColor(R.color.red));
        } else {
            mTvPercentChange7dValue.setText("+" + percent7d + "%" + getString(R.string.arrow_up));
            mTvPercentChange7dValue.setTextColor(getResources().getColor(R.color.green));
        }

        return view;
    }

    private String formattingDoubleValues(String rawValue) {
        return getString(R.string.usd_symbol) + " " + String.format(Locale.getDefault(), "%1$,.2f",
                Double.parseDouble(rawValue));
    }

    private void setListeners() {
    }

    @Override
    public void setList(ArrayList list) {

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showProgressIndicator() {
//        mProgressDialog = DialogFactory.createProgressDialog(getContext(), R.string.loading);
//        mProgressDialog.show();
//        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mCoinsPresenter != null) {
//            mCoinsPresenter.unsubscribe();
//        }
//
//        if (!searchDisposable.isDisposed()) {
//            searchDisposable.dispose();
//        }
    }
}
