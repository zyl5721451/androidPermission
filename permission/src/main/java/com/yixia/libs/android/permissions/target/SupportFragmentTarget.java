
package com.yixia.libs.android.permissions.target;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


public class SupportFragmentTarget implements Target {

    private Fragment mFragment;

    public SupportFragmentTarget(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public Context getContext() {
        return mFragment.getContext();
    }

    public Fragment getFragment(){
        return mFragment;
    }
    @Override
    public boolean shouldShowRationalePermissions(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        for (String permission : permissions) {
            boolean rationale = mFragment.shouldShowRequestPermissionRationale(permission);
            if (rationale) return true;
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        mFragment.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mFragment.startActivityForResult(intent, requestCode);
    }

}
