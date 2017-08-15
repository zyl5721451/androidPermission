
package com.yixia.libs.android.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.yixia.libs.android.permissions.target.AppActivityTarget;
import com.yixia.libs.android.permissions.target.SupportFragmentTarget;
import com.yixia.libs.android.permissions.target.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionRequest implements RationaleListener {

    private static final String TAG = PermissionRequest.class.getSimpleName();

    private Target target;

    private int mRequestCode;
    private String[] mPermissions;
    private RequestResultListener requestResultListener;
    private String[] mDeniedPermissions;
    private int mRationalResTitle;
    private int mRationalResMsg;
    private boolean mNeedShowSetting;
    private boolean mNeedDestroyActivity;

    PermissionRequest(Target target) {
        if (target == null) {
            throw new IllegalArgumentException("The target can not be null.");
        }
        this.target = target;
    }

    @NonNull
    public PermissionRequest permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public String[] getmPermissions() {
        return mPermissions;
    }

    public int getmRequestCode() {
        return mRequestCode;
    }

    @NonNull
    public PermissionRequest requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @NonNull
    public PermissionRequest rationalResTitle(@StringRes int resTitle) {
        this.mRationalResTitle = resTitle;
        return this;
    }

    @NonNull
    public PermissionRequest rationalResMsg(@StringRes int resMsg) {
        this.mRationalResMsg = resMsg;
        return this;
    }

    @NonNull
    public PermissionRequest needShowSetting(boolean needShow) {
        this.mNeedShowSetting = needShow;
        return this;
    }
    @NonNull
    public PermissionRequest needDestroyActivity(boolean needDestroy) {
        this.mNeedDestroyActivity = needDestroy;
        return this;
    }

    @NonNull
    public PermissionRequest requestListener(RequestResultListener listener) {
        this.requestResultListener = listener;
        return this;
    }


    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackSucceed();
        } else {
            mDeniedPermissions = getDeniedPermissions(target.getContext(), mPermissions);
            // Denied mPermissions size > 0.
            if (mDeniedPermissions.length > 0) {
                // Remind users of the purpose of mPermissions.
                boolean showRationale = target.shouldShowRationalePermissions(mDeniedPermissions);
                if (showRationale) {
                    createRationaleDialog(target.getContext(),this).show();
                }else{
                    resume();
                }
            } else { // All permission granted.
                callbackSucceed();
            }
        }
    }

    /**
     * 创建默认的提示dialog
     * @param context
     * @param rationale
     * @return
     */
    public RationaleDialog createRationaleDialog(@NonNull Context context, RationaleListener rationale) {
        RationaleDialog rationaleDialog = new RationaleDialog(context, rationale);
        if(mRationalResMsg>0) {
            rationaleDialog.setMessage(mRationalResMsg);
        }
        if(mRationalResTitle>0) {
            rationaleDialog.setTitle(mRationalResTitle);
        }
        return rationaleDialog;
    }


    private String[] getDeniedPermissions(Context context, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions){
            if (!AndPermission.hasPermission(context,permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList.toArray(new String[deniedList.size()]);
    }


    /**
     * 请求权限的操作
     */
    @Override
    public void resume() {
        if (target instanceof AppActivityTarget) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) target.getContext()).requestPermissions(mDeniedPermissions, mRequestCode);
            }
        } else if (target instanceof SupportFragmentTarget) {
            (((SupportFragmentTarget) target).getFragment()).requestPermissions(mDeniedPermissions, mRequestCode);
        } else {
            throw new IllegalArgumentException("caller must be activity or fragment");
        }
    }

    /**
     * 提示对话框的关闭操作，交给{{@link #onRequestPermissionsResult(String[], int[])}}来处理
     */
    @Override
    public void cancle() {
        int[] results = new int[mPermissions.length];
        boolean hasPermission;
        for (int i = 0; i < mPermissions.length; i++){
            hasPermission = AndPermission.hasPermission(target.getContext(),mPermissions[i]);
            results[i] = hasPermission?PackageManager.PERMISSION_GRANTED:PackageManager.PERMISSION_DENIED;
        }
        onRequestPermissionsResult(mPermissions,results);
    }


    /**
     * 对Activity中请求回调的处理，获取deniedList
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permissions[i]);
            }
        }
        if (deniedList.isEmpty()){
            callbackSucceed();
        }else{
            callbackFailed(deniedList);
        }
    }

    private void callbackSucceed() {
        if (requestResultListener != null) {
            requestResultListener.onPermissionGranted(mRequestCode,Arrays.asList(mPermissions));
        }
    }

    private void callbackFailed(List<String> deniedList) {
        if (requestResultListener != null) {
            if(shouldShowSetting(target,deniedList)) {
                new SettingDialog(target.getContext(), new SettingExecutor(target,mNeedDestroyActivity)).show();
            }else {
                requestResultListener.onPermissionDenied(mRequestCode,deniedList);
            }
        }
    }
    /**
     * 是否需要展示提示Dialog
     * @return 被拒绝的权限不需要展示rational，则是拒绝了两次,需要展示。或者需要展示setting
     */
    public boolean shouldShowSetting(Target target, List<String> deniedPermissions) {
        if(!mNeedShowSetting) {
            return false;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = target.shouldShowRationalePermissions(permission);
            if (!rationale) return true;
        }
        return false;
    }

}