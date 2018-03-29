package com.crypto.cryptoinfo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import com.amulyakhare.textdrawable.TextDrawable;
import com.crypto.cryptoinfo.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.crypto.cryptoinfo.utils.Constants.CHART_IMAGE_URL;


public class Utils {

    public static void setToolbarIconsColor(Context context, Menu menu, int color){
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
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
            // handle exception
        }

        return bitmap;
    }

    public static Drawable buildTextDrawable(Context context, String label){
        return TextDrawable.builder()
                .beginConfig()
                .textColor(context.getResources().getColor(R.color.textDisabled))
                .fontSize(35)
                .endConfig()
                .buildRoundRect(label.length() <= 3 ? label : label.substring(0, 3), Color.LTGRAY, 64);
    }

    public static String longToDateTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String getChartImageUrl(String coinNumId){
        return CHART_IMAGE_URL + coinNumId + ".png";
    }
}
