package com.crypto.cryptoinfo.ui.fragment.chartsCoinFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.PointTimePrice;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.tools.HourAxisValueFormatter;
import com.crypto.cryptoinfo.tools.XValueFormatter;
import com.crypto.cryptoinfo.tools.YValueFormatter;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.customViews.CustomMarkerView;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

import static com.crypto.cryptoinfo.utils.Constants.BTC;

public class ChartsCoinFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();
    private CoinsPresenter mCoinsPresenter;
    private CoinPojo mCoinPojo;

    @BindView(R.id.chart_coin)
    LineChart lineChart;

    @BindView(R.id.loading_indicator)
    AVLoadingIndicatorView progressBar;

    @BindView(R.id.rg)
    RadioRealButtonGroup radioRealButtonGroup;

    private int index;

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

        return view;
    }

    private void setListeners() {
        radioRealButtonGroup.setOnClickedButtonListener((button, position) -> {
            String pastTime;
            switch (position) {
                case 0:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.SIX_HOURS);
                    break;
                case 1:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.ONE_DAY);
                    break;
                case 2:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.SEVEN_DAYS);
                    break;
                case 3:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.ONE_MONTH);
                    break;
                case 4:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.SIX_MONTHS);
                    break;
                case 5:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.ONE_YEAR);
                    break;
                default:
                    pastTime = String.valueOf(System.currentTimeMillis() - Constants.SIX_HOURS);
                    break;

            }
            index = position;
            mCoinsPresenter.getChartsData(mCoinPojo.getId(), pastTime);
        });
    }

    @Override
    public void setList(ArrayList list) {

        progressBar.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);

        ArrayList<Entry> yVals = new ArrayList<>();

        if (mCoinPojo.getSymbol().equals(BTC) && SharedPreferencesHelper.getInstance().getCurrentCurrency().equals(BTC)) {
            yVals.add(new Entry(Float.parseFloat(((ArrayList<PointTimePrice>) list).get(0).getUnixTime()), 0.0f));
            for (PointTimePrice p : (ArrayList<PointTimePrice>) list) {
                yVals.add(new Entry(Float.parseFloat(p.getUnixTime()), Float.parseFloat(p.getPriceUsd())));
            }
            yVals.add(new Entry(Float.parseFloat(((ArrayList<PointTimePrice>) list).get(0).getUnixTime()), 2.0f));
        } else {
            for (PointTimePrice p : (ArrayList<PointTimePrice>) list) {
                yVals.add(new Entry(Float.parseFloat(p.getUnixTime()), Float.parseFloat(p.getPriceUsd())));
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setDrawFilled(true);
        set1.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient_chart));

//        set1.setFillAlpha(110);
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setDrawCircleHole(true);
//        set1.setFillColor(Color.RED);
        set1.setDrawCircles(false);
        set1.setCubicIntensity(0.2f);

        set1.setLineWidth(1f);
//        set1.setCircleRadius(5f);
//        set1.setCircleHoleRadius(2.5f);
        set1.setColor(ContextCompat.getColor(getContext(), R.color.set_chart_color));
//        set1.setCircleColor(Color.WHITE);
        set1.setHighlightEnabled(true);
        set1.setHighLightColor(ContextCompat.getColor(getContext(), R.color.colorTextDefault));
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setHighlightLineWidth(0.5f);

        set1.setDrawValues(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);

        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(lineChart, data);
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

    private void setupChart(LineChart chart, LineData data) {

        ((LineDataSet) data.getDataSetByIndex(0)).setDrawCircleHole(true);

        // no description text
        chart.getDescription().setEnabled(false);
        chart.setDrawMarkers(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.marker_view);
        customMarkerView.setChartView(chart);
        chart.setMarker(customMarkerView);

//        chart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));

//         mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setVisibleXRangeMaximum(7);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
//        chart.setViewPortOffsets(0, 0, 0, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

//        chart.setAutoScaleMinMaxEnabled(true);
//        chart.getAxisLeft().setDrawTopYLabelEntry(true);

        chart.getAxisRight().setEnabled(false);
//        chart.getAxisRight().setDrawZeroLine(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setSpaceBottom(20);
        yAxis.setSpaceTop(2);
        yAxis.setYOffset(-7f);
        yAxis.setValueFormatter(new YValueFormatter());
//        yAxis.setAxisMinimum(data.getDataSetByIndex(0).getYMin() - data.getDataSetByIndex(0).getYMin() / 2000);
//        yAxis.setAxisMinimum(data.getDataSetByIndex(0).getYMin());
//        yAxis.setAxisMaximum(data.getDataSetByIndex(0).getYMax());
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setTextColor(getResources().getColor(R.color.colorTextDefault));

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.colorTextDefault));
//        xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setAxisMinimum();

//        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(Long.parseLong(mPointList.get(0).getUnixTime()));
        xAxis.setValueFormatter(new XValueFormatter(index));

//        SimpleDateFormat mDataFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//        mDataFormat.setTimeZone(TimeZone.getDefault());
//        Date date = new Date(Long.parseLong(mPointList.get(0).getUnixTime()));
//
//        Log.d(TAG, mDataFormat.format(date));

//        chart.moveViewToX(200f);

//        chart.setDrawBorders(true);
//        chart.setBorderWidth(1f);
//        chart.setBorderColor(ContextCompat.getColor(getContext(), R.color.red));

        // animate calls invalidate()...
        chart.animateX(500);
//        chart.invalidate();
//        chart.animateY(1000);
    }
}
