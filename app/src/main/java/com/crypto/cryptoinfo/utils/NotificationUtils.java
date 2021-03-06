package com.crypto.cryptoinfo.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;

import java.util.Objects;

import static com.crypto.cryptoinfo.utils.Constants.COIN;

public class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();
    public static final int COIN_NOTIFICATION_ID = 1138;
    public static final int ACTION_COIN_NOTIF_PENDING_INTENT_REQUEST_ID = 1;

    public static void create(Context context,
                              int id,
                              Intent intent,
                              String contentTitle,
                              String contentText) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent p = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        Notification n = builder.build();
        manager.notify(id, n);

        Log.d(TAG, "Notification create successfully");
    }

    public static void createStackNotification(Context context,
                                               int id,
                                               String groupId,
                                               Intent intent,
                                               int smallIcon,
                                               String contentTitle,
                                               String contentText) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent p = intent != null
                ? PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                : null;

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setGroup(groupId)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        Notification n = builder.build();
        manager.notify(id, n);

        Log.d(TAG, "Notification create successfully");
    }

    public static void create(Context context, int smallIcon, String contentTitle, String contentText) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        Notification n = builder.build();
        manager.notify(0, n);

        Log.d(TAG, "Notification create successfully");
    }

    public static void sendNotificationForPriceChange(@NonNull Context context, CoinPojo coinPojo, String symbol, String message) {
        Log.d(TAG, "sendNotification: ");
        Intent intent = new Intent(context, CoinInfoActivity.class);
        intent.putExtra(COIN, coinPojo);
        NotificationUtils.create(context, symbol.hashCode(), intent, context.getString(R.string.app_name), message);
        App.dbInstance.getAlertCoinDao().deleteAlert(symbol);

    }

    public static void cancel(Context context, int id) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }

    public static void cancelAll(Context context) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancelAll();
    }
}
