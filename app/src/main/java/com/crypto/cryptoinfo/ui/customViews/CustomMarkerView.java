package com.crypto.cryptoinfo.ui.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.utils.ScreenUtils;
import com.crypto.cryptoinfo.utils.Utils;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private TextView tvPrice;
    private TextView tvDate;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, @LayoutRes int layoutResource) {
        super(context, layoutResource);
        tvPrice = findViewById(R.id.tv_marker_price);
        tvDate = findViewById(R.id.tv_marker_date);
    }

    @Override
    public void setOffset(MPPointF offset) {
        super.setOffset(offset);
    }

    @Override
    public void setOffset(float offsetX, float offsetY) {
        super.setOffset(offsetX, offsetY);
    }

    @Override
    public MPPointF getOffset() {
        return super.getOffset();
    }

    @Override
    public void setChartView(Chart chart) {
        super.setChartView(chart);
    }

    @Override
    public Chart getChartView() {
        return super.getChartView();
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return super.getOffsetForDrawingAtPoint(posX, posY);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvPrice.setText(Utils.formatPrice(String.valueOf(e.getY())));
        tvDate.setText(Utils.longToDateTime((long) e.getX(), "MMM dd y, HH:mm"));
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        // Check marker position and update offsets.
//        super.draw(canvas, posX, posY);
//        getOffsetForDrawingAtPoint(posX, posY);
        int w = getWidth();
        int h = getHeight();
        if ((getResources().getDisplayMetrics().widthPixels - posX) < w) {
            posX -= w;
        }
//        posY =
        posY = getChartView().getY() + getChartView().getHeight() - h - 46;
        // translate to the correct position and draw
        canvas.translate(posX, posY);
        draw(canvas);
        canvas.translate(-posX, -posY);
    }
}
