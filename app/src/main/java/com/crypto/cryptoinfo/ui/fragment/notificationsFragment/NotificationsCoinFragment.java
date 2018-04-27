package com.crypto.cryptoinfo.ui.fragment.notificationsFragment;


import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.background.service.ChangeCoinsPriceJobService;
import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crypto.cryptoinfo.utils.Constants.BTC;
import static com.crypto.cryptoinfo.utils.Constants.EUR;
import static com.crypto.cryptoinfo.utils.Constants.USD;

public class NotificationsCoinFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.tv_price_high)
    TextView tvPriceHigh;

    @BindView(R.id.tv_price_low)
    TextView tvPriceLow;

    @BindView(R.id.seekbar_high)
    SeekBar seekBarHigh;

    @BindView(R.id.seekbar_low)
    SeekBar seekBarLow;

    @BindView(R.id.checkbox_high)
    CheckBox checkBoxHigh;

    @BindView(R.id.checkbox_low)
    CheckBox checkBoxLow;

    double currentValue;
    double seekBarValueHigh;
    double seekBarValueLow;

    private CoinPojo mCoinPojo;
    private static int JOB_ID = 101;
    private JobInfo mJobInfo;
    private JobScheduler mJobScheduler;

    public NotificationsCoinFragment() {
        // Required empty public constructor
    }

    public static NotificationsCoinFragment newInstance() {
        return new NotificationsCoinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_notifications_coin, container, false);
        ButterKnife.bind(this, view);
        setListeners();
        prepareSeekBars();

        mCoinPojo = ((CoinInfoActivity) getActivity()).getCoinPojo();
        AlertCoinPojo alertCoinPojo = App.dbInstance.getAlertCoinDao().getAlertCoin(mCoinPojo.getSymbol());

        String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();
        String price;
        switch (currentCurrency) {
            case USD:
                price = Utils.formatPrice(mCoinPojo.getPriceUsd());
                currentValue = Double.parseDouble(mCoinPojo.getPriceUsd());
                break;
            case EUR:
                price = Utils.formatPrice(mCoinPojo.getPriceEur());
                currentValue = Double.parseDouble(mCoinPojo.getPriceEur());
                break;
            case BTC:
                price = Utils.formatPrice(mCoinPojo.getPriceBtc());
                currentValue = Double.parseDouble(mCoinPojo.getPriceBtc());
                break;
            default:
                price = Utils.formatPrice(mCoinPojo.getPriceUsd());
                currentValue = 1.0;
                break;
        }

//        tvPriceHigh.setText(price);
//        tvPriceLow.setText(price);

        if (alertCoinPojo != null) {
            if (alertCoinPojo.getHigh() != 0.0) {
                checkBoxHigh.setChecked(true);
                seekBarValueHigh = alertCoinPojo.getHigh();
                int progress = (int) (((alertCoinPojo.getHigh() - currentValue) * 5000d) / (0.5 * currentValue));
                seekBarHigh.setProgress(progress);
            }

            if (alertCoinPojo.getLow() != 0.0) {
                checkBoxLow.setChecked(true);
                seekBarValueLow = alertCoinPojo.getLow();
                int progress = (int) (((alertCoinPojo.getLow() - 0.5 * currentValue) * 5000d) / (0.5 * currentValue));

                Log.d(TAG, "currentValue: " + currentValue);
                Log.d(TAG, "alertCoinPojo.getLow(): " + alertCoinPojo.getLow());
                Log.d(TAG, "progress: " + progress);
                seekBarLow.setProgress(progress);
            }
        }

        return view;
    }

    private void setListeners() {

        checkBoxHigh.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Log.d(TAG, "setListeners: seekBarValueHigh " + seekBarValueHigh);
                seekBarHigh.setEnabled(true);
            } else {
                seekBarHigh.setEnabled(false);
                seekBarHigh.setProgress(0);
            }
        });
        checkBoxLow.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                seekBarLow.setEnabled(true);
            } else {
                seekBarLow.setEnabled(false);
                seekBarLow.setProgress(seekBarLow.getMax());
            }
        });
        seekBarHigh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean byUser) {
                updateUIHigh(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarLow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean byUser) {
                updateUILow(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
    public void notifyForChanges() {

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
    }

    @Override
    public void hideProgressIndicator() {

    }

    private void prepareSeekBars() {
        //changing progress of seekBarHigh to call seekBar listener and update ui
        seekBarHigh.setProgress(50);
        seekBarHigh.setProgress(0);

        seekBarLow.setProgress(seekBarLow.getMax());
        if (checkBoxHigh.isChecked()) {
            seekBarHigh.setEnabled(true);
        } else {
            seekBarHigh.setEnabled(false);
        }
        if (checkBoxLow.isChecked()) {
            seekBarLow.setEnabled(true);
        } else {
            seekBarLow.setEnabled(false);
        }
    }

    private void updateUIHigh(int i) {
        new Handler(Looper.getMainLooper()).post(() -> {
            double percentChange = ((double) i) / 100.0d;
            seekBarValueHigh = (currentValue * (100.0d + percentChange)) / 100.0d;
            tvPriceHigh.setText(Utils.formatPrice(String.valueOf(seekBarValueHigh)));
        });
    }

    private void updateUILow(int i) {
        new Handler(Looper.getMainLooper()).post(() -> {
            double percentChange = ((double) i) / 100.0d;
            seekBarValueLow = currentValue - (currentValue * (50.0d - percentChange)) / 100.0d;
            tvPriceLow.setText(Utils.formatPrice(String.valueOf(seekBarValueLow)));
        });
    }

    private void startJob() {
        Log.d(TAG, "startJob: ");
        ComponentName mComponentName = new ComponentName(getContext(), ChangeCoinsPriceJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, mComponentName);
        mJobInfo = builder.setPeriodic(20000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        mJobScheduler.schedule(mJobInfo);
    }

    private void stopJob() {
        Log.d(TAG, "stopJob: ");
        mJobScheduler.cancel(JOB_ID);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        if (!checkBoxHigh.isChecked() && !checkBoxLow.isChecked()) {
            App.dbInstance.getAlertCoinDao().deleteAlert(mCoinPojo.getSymbol());
        } else {
            Log.d(TAG, "onDestroy: SAVE");
            AlertCoinPojo alertCoinPojo = new AlertCoinPojo();
            alertCoinPojo.setSymbol(mCoinPojo.getSymbol());
            if (checkBoxHigh.isChecked()) {
                alertCoinPojo.setHigh(seekBarValueHigh);
            }
            if (checkBoxLow.isChecked()) {
                alertCoinPojo.setLow(seekBarValueLow);
            }
            App.dbInstance.getAlertCoinDao().insertAll(alertCoinPojo);
            Log.d(TAG, "onDestroy: alertCoinPojo " + alertCoinPojo);
        }

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
