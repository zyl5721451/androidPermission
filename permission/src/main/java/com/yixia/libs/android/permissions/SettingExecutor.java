
package com.yixia.libs.android.permissions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.yixia.libs.android.permissions.target.AppActivityTarget;
import com.yixia.libs.android.permissions.target.Target;


class SettingExecutor{

    private Target target;
    private boolean needDestroy;

    SettingExecutor(@NonNull Target target, boolean need) {
        this.target = target;
        this.needDestroy = need;
    }

    public void execute() {
        Context context = target.getContext();
        if(isMIUI()) {
            try {
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                target.startActivityForResult(localIntent, AndPermission.REQUEST_CODE_SETTING);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                target.startActivityForResult(intent, AndPermission.REQUEST_CODE_SETTING);
            }
        }else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            target.startActivityForResult(intent, AndPermission.REQUEST_CODE_SETTING);
        }


    }


    /**
     * 检查手机是否是miui
     * @return
     */
    public static boolean isMIUI(){
        String device = Build.MANUFACTURER;
        if (device.equals( "Xiaomi" ) ) {
            return true;
        }
        else{
            return false;
        }
    }


    public void cancle() {
        if (target instanceof AppActivityTarget &&needDestroy) {
            ((Activity) target.getContext()).finish();
        }
    }
}