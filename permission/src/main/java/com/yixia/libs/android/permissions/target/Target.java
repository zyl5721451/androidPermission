
package com.yixia.libs.android.permissions.target;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


public interface Target {

    Context getContext();

    boolean shouldShowRationalePermissions(@NonNull String... permissions);

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

}
