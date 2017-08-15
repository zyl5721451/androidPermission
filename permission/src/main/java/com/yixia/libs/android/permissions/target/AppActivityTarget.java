
package com.yixia.libs.android.permissions.target;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

public class AppActivityTarget implements Target {

    private Activity mActivity;

    public AppActivityTarget(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public boolean shouldShowRationalePermissions(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        for (String permission : permissions) {
            boolean rationale = mActivity.shouldShowRequestPermissionRationale(permission);
            if (rationale) return true;
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

}
