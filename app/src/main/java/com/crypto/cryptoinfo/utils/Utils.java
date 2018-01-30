package com.crypto.cryptoinfo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;


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
}
