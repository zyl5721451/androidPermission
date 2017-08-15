
package com.yixia.libs.android.permissions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.yixia.libs.android.R;


public class SettingDialog {

    private AlertDialog.Builder mBuilder;
    private SettingExecutor mSettingService;

    SettingDialog(@NonNull Context context, @NonNull SettingExecutor settingService) {
        mBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.permission_dialog_title)
                .setMessage(R.string.denied_dialog_content)
                .setPositiveButton(R.string.denied_dialog_ok, mClickListener)
                .setNegativeButton(R.string.denied_dialog_cancle, mClickListener);
        this.mSettingService = settingService;
    }

    @NonNull
    public AlertDialog.Builder setTitle(@NonNull String title) {
        mBuilder.setTitle(title);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setTitle(@StringRes int title) {
        mBuilder.setTitle(title);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setMessage(@NonNull String message) {
        mBuilder.setMessage(message);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setMessage(@StringRes int message) {
        mBuilder.setMessage(message);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setNegativeButton(@NonNull String text, @Nullable DialogInterface.OnClickListener
            negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setNegativeButton(@StringRes int text, @Nullable DialogInterface.OnClickListener
            negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setPositiveButton(@NonNull String text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return mBuilder;
    }

    @NonNull
    public AlertDialog.Builder setPositiveButton(@StringRes int text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return mBuilder;
    }

    public void show() {
        mBuilder.show();
    }

    /**
     * The dialog's btn click listener.
     */
    private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    mSettingService.cancle();
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    mSettingService.execute();
                    break;
                default:
                    break;
            }
        }
    };
}
