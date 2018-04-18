package com.crypto.cryptoinfo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.crypto.cryptoinfo.App;

import java.util.List;

public class AndroidUtils {

    public static boolean isNotificationServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isServiceRunning?", false + "");
        return false;
    }

    public static boolean isAppForeground() {
        ActivityManager manager =
                (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(App.getInstance().getPackageName());
            }
        }
        return false;
    }

    public static void openMarket(Context context) {
        try {
//            context.startActivity(new Intent(Intent.ACTION_VIEW,
//                    Uri.parse(context.getString(R.string.market_uri, context.getPackageName()))));
        } catch (ActivityNotFoundException e) {
//            context.startActivity(new Intent(Intent.ACTION_VIEW,
//                    Uri.parse(context.getString(R.string.play_store_uri, context.getPackageName()))));
        }
    }

    public static void share(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            //do nothing
        }
    }

    public static void sendEmail(Context context, String reportTo, String reportSubject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", reportTo, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, reportSubject);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
//            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)));
        } catch (ActivityNotFoundException e) {
            //do nothing
        }
    }

    public static void showFileChooser(Activity activity, String title, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(
                    Intent.createChooser(intent, title), requestCode);
        } catch (ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void onPreDraw(final View view, final Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                runnable.run();
                return true;
            }
        });
    }
}
