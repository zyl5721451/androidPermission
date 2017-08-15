package permission.androidpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.yixia.libs.android.permissions.AndPermission;
import com.yixia.libs.android.permissions.RequestResultListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RequestResultListener{
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndPermission.createRequest(MainActivity.this).
                        requestCode(AndPermission.REQUEST_PERMISSION_CODE_HOME).
                        needDestroyActivity(true).
                        needShowSetting(true).
                        requestListener(MainActivity.this).
                    permission(Manifest.permission.CAMERA).
                        start();
            }
        });
    }

    @Override
    public void onPermissionGranted(int requestCode, @NonNull List<String> grantPermissions) {
        Snackbar.make(fab,"onPermissionGranted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onPermissionDenied(int requestCode, @NonNull List<String> deniedPermissions) {
        Snackbar.make(fab,"onPermissionDenied", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestCallback(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        processSettingResult(requestCode);
    }
    private void processSettingResult(int requestCode) {
        switch (requestCode) {
            case AndPermission.REQUEST_CODE_SETTING:
                if(AndPermission.hasPermission(this,AndPermission.getPermissions())) {
                    onPermissionGranted(AndPermission.getRequestCode(), Arrays.asList(AndPermission.getPermissions()));
                }else {
                    onPermissionDenied(AndPermission.getRequestCode(),Arrays.asList(AndPermission.getPermissions()));
                }
                break;
        }
    }

}
