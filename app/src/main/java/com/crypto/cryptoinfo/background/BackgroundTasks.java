package com.crypto.cryptoinfo.background;

import android.content.Context;
import android.util.Log;

public class BackgroundTasks {

    private static final String TAG = BackgroundTasks.class.getSimpleName();

    public static final String ACTION_GET_COINS_FOT_NOTIFICATIONS = "ACTION_GET_COINS_FOT_NOTIFICATIONS";
    
    public static void executeTask(Context context, String action) {
        if (ACTION_GET_COINS_FOT_NOTIFICATIONS.equals(action)) {
            getCoinsForNotifications(context);
        } else {
            Log.d(TAG, "executeTask: action nothing");
        }
    }

    private static void getCoinsForNotifications(Context context) {
        //getting coins
    }
}
