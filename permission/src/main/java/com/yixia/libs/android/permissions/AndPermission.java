
package com.yixia.libs.android.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.yixia.libs.android.permissions.target.AppActivityTarget;
import com.yixia.libs.android.permissions.target.SupportFragmentTarget;
import com.yixia.libs.android.permissions.target.Target;

import java.util.Arrays;
import java.util.List;


public class AndPermission {
    public static final int REQUEST_CODE_SETTING = 300;//进入设置界面的返回码
    public static final int REQUEST_PERMISSION_CODE_STORAGE = 301;//外部存储权限请求码
    public static final int REQUEST_PERMISSION_CODE_RECORD = 302;//拍摄页面的请求码
    public static final int REQUEST_PERMISSION_CODE_CAMERA = 303;//相机权限请求码
    public static final int REQUEST_PERMISSION_CODE_HOME = 304;//首页权限请求码
    public static final int REQUEST_PERMISSION_CODE_READ_CONTACTS = 305;//读取通讯录权限请求码


    private static PermissionRequest permissionRequest;

    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context,Arrays.asList(permissions));
    }

    /**
     * 检查是否有指定的权限列表，有一个权限没有，就返回false
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
            result = ContextCompat.checkSelfPermission(context, permission);
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }



    public static  @NonNull
    PermissionRequest createRequest(@NonNull Object objects) {
        Target target;
        if(objects instanceof Activity) {
            target = new AppActivityTarget((Activity) objects);
        }else if(objects instanceof Fragment) {
            target = new SupportFragmentTarget((Fragment) objects);
        }else if(objects instanceof FragmentActivity) {
            target = new AppActivityTarget((FragmentActivity) objects);
        }else {
            throw new IllegalArgumentException("caller must be activity or fragment");
        }
        permissionRequest = new PermissionRequest(target);
        return permissionRequest;
    }

    public static PermissionRequest getPermissionRequest() {
        return permissionRequest;
    }
    public static void onRequestCallback(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionRequest.onRequestPermissionsResult(permissions,grantResults);
    }

    public static String[] getPermissions() {
        return permissionRequest.getmPermissions();
    }
    public static int getRequestCode() {
        return permissionRequest.getmRequestCode();
    }

}
