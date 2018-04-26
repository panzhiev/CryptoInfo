package com.crypto.cryptoinfo.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.ui.ChangeCurrencyListener;

import static com.crypto.cryptoinfo.utils.Constants.SKIP;


public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, (DialogInterface dialog, int which) -> dialog.dismiss());
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static AlertDialog createAttentionDialog(Context context,
                                                    ChangeCurrencyListener listener) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.checkbox_for_attention_dialog, null);
        CheckBox checkBox = layout.findViewById(R.id.checkbox_skip);

        return new AlertDialog.Builder(context)
                .setView(layout)
                .setTitle(context.getString(R.string.attention))
                .setMessage(context.getString(R.string.attention_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), (DialogInterface dialog, int which) -> {
                    listener.agreeWithChanging();
                    if (checkBox.isChecked())
                        SharedPreferencesHelper.getInstance().putSkip(1);
                    dialog.dismiss();
                })
                .setNegativeButton(context.getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .create();
    }
}
