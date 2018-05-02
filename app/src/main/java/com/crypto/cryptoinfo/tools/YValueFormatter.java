package com.crypto.cryptoinfo.tools;

import com.crypto.cryptoinfo.utils.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class YValueFormatter implements IAxisValueFormatter {
    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return formatted value
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return Utils.formatPrice(String.valueOf(value));
    }
}