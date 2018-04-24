package com.crypto.cryptoinfo.utils;

import android.arch.persistence.room.util.StringUtil;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.crypto.cryptoinfo.utils.Constants.CHART_IMAGE_URL;
import static com.crypto.cryptoinfo.utils.Constants.COIN_IMAGE_URL_128x128;
import static com.crypto.cryptoinfo.utils.Constants.PERCENT_SYMBOL;


public class Utils {

    private static final String TAG = "Utils";

    public static void setToolbarIconsColor(Context context, Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setTint(context.getResources().getColor(color));
            }
        }
    }

    public static Bitmap getBitmapFromCryptoIconsAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open("cryptoIcons/" + fileName + ".png");
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Drawable buildTextDrawable(Context context, String label) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(context.getResources().getColor(R.color.textDisabled))
                .fontSize(35)
                .endConfig()
                .buildRoundRect(label.length() <= 3 ? label : label.substring(0, 3), Color.LTGRAY, 64);
    }

    public static String longToDateTime(long timestamp) {
        return longToDateTime(timestamp, "dd.MM.yyyy HH:mm:ss");
    }

    public static String longToDateTime(long timestamp, String dateFormatPattern) {
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat(dateFormatPattern, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getChartImageUrl(String coinNumId) {
        return CHART_IMAGE_URL + coinNumId + ".png";
    }

    public static String getCoinImageUrl(String coinNumId) {
        return COIN_IMAGE_URL_128x128 + coinNumId + ".png";
    }

    public static String formatPrice(String unformattedPrice) {

        String symbol = SharedPreferencesHelper.getInstance().getCurrentCurrencySymbol();

        if (unformattedPrice == null || unformattedPrice.isEmpty()) return "? ".concat(symbol);

        unformattedPrice = unformattedPrice.toLowerCase();

        if (unformattedPrice.startsWith("0") || unformattedPrice.contains("e-")) {
            return String.format(Locale.getDefault(), "%1$,.7f", Double.parseDouble(unformattedPrice)).concat(" ").concat(symbol);
        } else {
            return String.format(Locale.getDefault(), "%1$,.2f", Double.parseDouble(unformattedPrice)).concat(" ").concat(symbol);
        }
    }

    public static String formatMarketCap(String unformattedMarketCap) {

        String symbol = SharedPreferencesHelper.getInstance().getCurrentCurrencySymbol();

        if (unformattedMarketCap != null && !unformattedMarketCap.isEmpty() && unformattedMarketCap.startsWith("0")) {
            return String.format(Locale.getDefault(), "%1$,.7f", Double.parseDouble(unformattedMarketCap)).concat(" ").concat(symbol);
        } else if (unformattedMarketCap != null && !unformattedMarketCap.isEmpty()) {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);

            return df.format(Double.parseDouble(unformattedMarketCap)).concat(" ").concat(symbol);
        } else {
            return "? ".concat(symbol);
        }
    }

    public static String formatMarketCapForBtc(String priceUsd, String priceBtc, String marketCapUsd) {

        String symbol = SharedPreferencesHelper.getInstance().getCurrentCurrencySymbol();

        if (priceUsd == null || priceBtc == null || marketCapUsd == null) {
            return "? ".concat(symbol);
        }

        Double priceUsdDouble = Double.parseDouble(priceUsd);
        Double priceBtcDouble = Double.parseDouble(priceBtc);
        Double marketCapUsdDouble = Double.parseDouble(marketCapUsd);

        String unformattedMarketCap = String.valueOf(priceBtcDouble * marketCapUsdDouble / priceUsdDouble);

        if (unformattedMarketCap != null && !unformattedMarketCap.isEmpty() && unformattedMarketCap.startsWith("0")) {
            return String.format(Locale.getDefault(), "%1$,.7f", Double.parseDouble(unformattedMarketCap)).concat(" ").concat(symbol);
        } else if (unformattedMarketCap != null && !unformattedMarketCap.isEmpty()) {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(symbols);
            df.setGroupingSize(3);

            return df.format(Double.parseDouble(unformattedMarketCap)).concat(" ").concat(symbol);
        }
        return "? ".concat(symbol);
    }

    public static void formatPercentChange(Context context, TextView tv, String percentChange) {

        if (percentChange != null) {
            if (percentChange.contains("-")) {
                tv.setText(percentChange.concat("%"));
                tv.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                tv.setText("+".concat(percentChange).concat("%"));
                tv.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            tv.setText("-");
            tv.setTextColor(context.getResources().getColor(R.color.colorTextDefault));
        }
    }

    public static String formatPercentChangeForMarkets(Double percentChange) {

        String formattedValue = "-%";
        if (percentChange != null) {
            formattedValue = String.format(Locale.US, "%.3f", percentChange).concat(PERCENT_SYMBOL);
        }
        return formattedValue;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static Drawable getTextDrawable(Context context, String symbol) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(context.getResources().getColor(R.color.textDisabled))
                .fontSize(35)
                .endConfig()
                .buildRoundRect(symbol.length() <= 3 ? symbol : symbol.substring(0, 3), Color.LTGRAY, 64);
    }
}
