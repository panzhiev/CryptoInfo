package com.crypto.cryptoinfo.tools;

import com.crypto.cryptoinfo.utils.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class XValueFormatter implements IAxisValueFormatter {

    public static final int SIX_HOURS = 0;
    public static final int ONE_DAY = 1;
    public static final int SEVEN_DAYS = 2;
    public static final int ONE_MONTH = 3;
    public static final int SIX_MONTHS = 4;
    public static final int ONE_YEAR = 5;

    private int index;

    private DateFormat mDataFormat;

    public XValueFormatter(int periodIndex) {

        index = periodIndex;
        String pattern = "";

        switch (index) {
            case SIX_HOURS:
                pattern = "HH:mm";
                break;
            case ONE_DAY:
                pattern = "HH:mm";
                break;
            case SEVEN_DAYS:
                pattern = "E, HH:mm";
                break;
            case ONE_MONTH:
                pattern = "d MMM";
                break;
            case SIX_MONTHS:
                pattern = "d MMM";
                break;
            case ONE_YEAR:
                pattern = "d MMM";
                break;
            default:
                pattern = "HH:mm";
                break;
        }

        mDataFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        mDataFormat.setTimeZone(TimeZone.getDefault());
    }

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
        return mDataFormat.format(new Date((long) value));
//        return Utils.formatPrice(String.valueOf(value));
    }
}