package com.omahagdg.dagger2demo.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.main.MainFragment;
import com.omahagdg.dagger2demo.permissions.Permission;
import com.omahagdg.dagger2demo.permissions.PermissionHelper;
import com.omahagdg.dagger2demo.permissions.PermissionRationaleDialog;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
        implements PermissionHelper.View, ActivityPresenter.View {

    private static final int ACTIVITY_SETTINGS_REQUEST = 5;

    @Inject PermissionHelper permissionHelper;
    @Inject ActivityPresenter activityPresenter;

    @Bind(R.id.main_container) FrameLayout container;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private Set<ActivityResultDelegate> resultDelegates = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ((MyApplication) getApplication()).getComponent().inject(this);
        permissionHelper.takeView(this);
        activityPresenter.takeView(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, MainFragment.newInstance(), "main_fragment")
                    .commit();
        }
    }

    @Override public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (ActivityResultDelegate resultDelegate : resultDelegates) {
            resultDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO settings
        return super.onOptionsItemSelected(item);
    }

    @Override public void registerActivityResultDelegate(ActivityResultDelegate delegate) {
        resultDelegates.add(delegate);
    }

    @Override public void unregisterActivityResultDelegate(ActivityResultDelegate delegate) {
        resultDelegates.remove(delegate);
    }

    @Override public void goToAppSystemSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        appSettingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(appSettingsIntent, ACTIVITY_SETTINGS_REQUEST);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        permissionHelper.dropView(this);
        activityPresenter.dropView(this);
    }

    // -------- Permissions ---------

    @TargetApi(Build.VERSION_CODES.M)
    @Override public boolean hasPermission(Permission permissionType) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permissionType.permissionString) == PERMISSION_GRANTED;
    }

    @Override public boolean shouldShowPermissionRationale(Permission permissionType) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this,
                permissionType.permissionString);
    }

    @Override public void requestPermission(Permission permissionType) {
        ActivityCompat.requestPermissions(this, new String[]{permissionType.permissionString},
                permissionType.id);
    }

    @Override public void showPermissionRationaleDialog(Permission permissionType) {
        PermissionRationaleDialog dialog = PermissionRationaleDialog.newInstance(permissionType);
        dialog.show(getSupportFragmentManager(), "rational_dialog");
        permissionHelper.setDialogShowing(true);
    }
}
