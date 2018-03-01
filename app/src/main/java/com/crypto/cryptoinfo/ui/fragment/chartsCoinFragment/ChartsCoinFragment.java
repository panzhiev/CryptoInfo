package com.crypto.cryptoinfo.ui.fragment.chartsCoinFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.PointTimePrice;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartsCoinFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();
    private CoinsPresenter mCoinsPresenter;
    private CoinPojo mCoinPojo;

    @BindView(R.id.chart_coin)
    LineChart lineChart;

    public ChartsCoinFragment() {
        // Required empty public constructor
    }

    public static ChartsCoinFragment newInstance() {
        return new ChartsCoinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        if (mCoinsPresenter == null) {
            mCoinsPresenter = new CoinsPresenter(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_charts_coin, container, false);
        ButterKnife.bind(this, view);
        setListeners();

        mCoinPojo = ((CoinInfoActivity) getActivity()).getCoinPojo();
        String pastTime = String.valueOf(System.currentTimeMillis() / 1000L - Constants.SIX_HOURS) + "000";
        Log.d(TAG, pastTime);

        mCoinsPresenter.getChartsData(mCoinPojo.getId(), pastTime);
        return view;
    }

    private void setListeners() {
    }

    @Override
    public void setList(ArrayList list) {

        ArrayList<Entry> yVals = new ArrayList<>();

        for (PointTimePrice p : (ArrayList<PointTimePrice>) list) {
            yVals.add(new Entry(Float.parseFloat(p.getUnixTime()), Float.parseFloat(p.getPriceUsd())));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setFillAlpha(110);

//        set1.setFillColor(Color.RED);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.75f);
//        set1.setCircleRadius(5f);
//        set1.setCircleHoleRadius(2.5f);
        set1.setColor(getResources().getColor(R.color.colorTextDefault));
//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);

        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(lineChart, data, getResources().getColor(R.color.colorPrimary));
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
        mCoinsPresenter.unsubscribe();
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

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColorHole(color);

        // no description text
        chart.getDescription().setEnabled(true);

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(true);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
//        chart.setViewPortOffsets(0, 0, 0, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(true);

        // animate calls invalidate()...
        chart.animateX(2500);
    }
}
