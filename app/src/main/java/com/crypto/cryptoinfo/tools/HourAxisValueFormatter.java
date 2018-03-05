package com.crypto.cryptoinfo.tools;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HourAxisValueFormatter implements IAxisValueFormatter {

    private long referenceTimestamp; // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    public HourAxisValueFormatter(long referenceTimestamp) {
//        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.mDataFormat.setTimeZone(TimeZone.getDefault());
//        this.mDate = new Date();

//        TimeZone tz = TimeZone.getDefault();
//        System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());

    }

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
//        long convertedTimestamp = (long) value;

        // Retrieve original timestamp
//        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        // Convert timestamp to hour:minute
        return getHour((long) value);
    }

    private String getHour(long timestamp) {
        try {

            mDate = new Date(timestamp);
//            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
}