package com.crypto.cryptoinfo.background.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crypto.cryptoinfo.background.service.NotificationService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationBroadcastReceiver.class.getSimpleName();
    public static final String ACTION_RESTART_SERVICE = "ACTION_RESTART_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: Service Stops!!!!!");
        context.startService(new Intent(context, NotificationService.class));
    }
}
