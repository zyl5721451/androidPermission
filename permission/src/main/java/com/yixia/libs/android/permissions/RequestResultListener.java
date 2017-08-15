package com.yixia.libs.android.permissions;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by yilong on 2017/8/14.
 */

public interface RequestResultListener {
    void onPermissionGranted(int requestCode, @NonNull List<String> grantPermissions);
    void onPermissionDenied(int requestCode, @NonNull List<String> deniedPermissions);
}
