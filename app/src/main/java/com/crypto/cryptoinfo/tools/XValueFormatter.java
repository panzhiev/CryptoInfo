package com.crypto.cryptoinfo.tools;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class XValueFormatter implements IAxisValueFormatter {

    private static final String TAG = "XValueFormatter";
    private long referenceTimestamp;
    private Date mDate;

    private static final int SIX_HOURS = 0;
    private static final int ONE_DAY = 1;
    private static final int SEVEN_DAYS = 2;
    private static final int ONE_MONTH = 3;
    private static final int SIX_MONTHS = 4;
    private static final int ONE_YEAR = 5;

    private DateFormat mDataFormat;

    public XValueFormatter(int periodIndex, long referenceTimestamp) {

        this.referenceTimestamp = referenceTimestamp;
        this.mDate = new Date();

        Log.d(TAG, "constructor init");

        String pattern = "";

        switch (periodIndex) {
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

        Log.d(TAG, "XValueFormatter: pattern " + pattern);

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

        long relativeTimestamp = (long) value;
        // Retrieve absolute timestamp (referenceTimestamp + relativeTimestamp)
        long originalTimestamp = referenceTimestamp + relativeTimestamp;
        return getHour(originalTimestamp);
    }

    /**
     * Get formatted hour from a timestamp.
     *
     * @param timestamp timestamp to format.
     * @return formatted hour.
     */
    private String getHour(long timestamp) {
        mDate.setTime(timestamp);
        return mDataFormat.format(mDate);
    }
}