package com.omahagdg.dagger2demo.main;

import android.support.annotation.NonNull;

import com.omahagdg.dagger2demo.base.ActivityResultDelegate;
import com.omahagdg.dagger2demo.permissions.Permission;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationPermissionHelper implements ActivityResultDelegate {

    private final LocationPermissionResultListener listener;

    LocationPermissionHelper(LocationPermissionResultListener listener) {
        this.listener = listener;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode == Permission.LOCATION.id) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED;
            listener.onLocationPermissionResult(granted);
        }
    }

    interface LocationPermissionResultListener {

        void onLocationPermissionResult(boolean granted);
    }
}
