package com.crypto.cryptoinfo.ui.fragment.notificationsFragment;


import android.app.job.JobInfo;
import android.content.ComponentName;
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

import static com.crypto.cryptoinfo.utils.Constants.USD;

public class NotificationsCoinFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.btn_save_changes)
    Button btnSaveChanges;

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

    double minValue;
    double currentValue;
    double maxValue;
    double seekBarValueHigh;
    double seekBarValueLow;

    private CoinPojo mCoinPojo;
    private AlertCoinPojo mAlertCoinPojo = new AlertCoinPojo();
    private static int JOB_ID = 101;
    private JobInfo mJobInfo;

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
        String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();

        String price;
        switch (currentCurrency) {
            case USD:
                price = Utils.formatPrice(mCoinPojo.getPriceUsd());
                tvPriceHigh.setText(price);
                tvPriceLow.setText(price);
                currentValue = Double.parseDouble(mCoinPojo.getPriceUsd());
                minValue = currentValue / 2.0;
                maxValue = currentValue * 2.0;
                break;
            default:
                tvPriceHigh.setText(mCoinPojo.getPriceUsd());
                tvPriceLow.setText(mCoinPojo.getPriceUsd());
                break;
        }

        return view;
    }

    private void setListeners() {

        checkBoxHigh.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                seekBarHigh.setEnabled(true);
            } else {
                seekBarHigh.setEnabled(false);
            }
        });

        checkBoxLow.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                seekBarLow.setEnabled(true);
            } else {
                seekBarLow.setEnabled(false);
            }
        });
        btnSaveChanges.setOnClickListener(v -> saveChanges());
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
                if (byUser) {
                    updateUILow(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void saveChanges() {

        if (!checkBoxHigh.isChecked() && !checkBoxLow.isChecked()) {
            App.dbInstance.getAlertCoinDao().deleteAlertCoinPojo(new AlertCoinPojo(mCoinPojo.getId(), 0.0, 0.0, true));
            //cancelling job dispatcher if alert table is empty
            if (App.dbInstance.getAlertCoinDao().getAll().isEmpty()) stopJob();
            Log.d(TAG, "saveChanges: return");
            return;
        }

        mAlertCoinPojo.setId(mCoinPojo.getId());
        //TODO: add radio button for checking is one time alert or not
        mAlertCoinPojo.setOneTime(true);
        if (checkBoxHigh.isChecked()) mAlertCoinPojo
                .setHigh(seekBarValueHigh);
        if (checkBoxLow.isChecked()) mAlertCoinPojo
                .setLow(seekBarValueLow);

        App.dbInstance.getAlertCoinDao().insertAll(mAlertCoinPojo);
        startJob();
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
//        mProgressDialog = DialogFactory.createProgressDialog(getContext(), R.string.loading);
//        mProgressDialog.show();
//        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {

    }

    private void prepareSeekBars() {
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

    private void updateUIHigh(final int i) {
        new Handler(Looper.getMainLooper()).post(() -> {
            double percentChange = ((double) i) / 100.0d;
            seekBarValueHigh = (currentValue * (100.0d + percentChange)) / 100.0d;
            tvPriceHigh.setText(Utils.formatPrice(String.valueOf(seekBarValueHigh)));
        });
    }

    private void updateUILow(final int i) {
        new Handler(Looper.getMainLooper()).post(() -> {
            double percentChange = ((double) i) / 100.0d;
            seekBarValueLow = currentValue - (currentValue * (50.0d - percentChange)) / 100.0d;
            tvPriceLow.setText(Utils.formatPrice(String.valueOf(seekBarValueLow)));
        });
    }

    private void startJob() {
        ComponentName mComponentName = new ComponentName(getContext(), ChangeCoinsPriceJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, mComponentName);
        mJobInfo = builder.setPeriodic(20000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        App.getJobScheduler().schedule(mJobInfo);
    }

    private void stopJob() {
        App.getJobScheduler().cancel(JOB_ID);
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
