package com.omahagdg.dagger2demo.permissions;

import com.omahagdg.dagger2demo.base.Presenter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PermissionHelper extends Presenter<PermissionHelper.View> {

    private boolean dialogShowing;

    @Inject public PermissionHelper() {

    }

    public boolean hasPermission(Permission permissionType) {
        return getView() != null && getView().hasPermission(permissionType);
    }

    public void requestPermission(Permission permissionType) {
        if (dialogShowing) return;
        requestPermissionOrShowRationale(permissionType);
    }

    private void requestPermissionOrShowRationale(Permission permissionType) {
        if (shouldShowPermissionRationale(permissionType)) {
            showPermissionRationaleDialog(permissionType);
        } else {
            requestPermissionInternal(permissionType);
        }
    }

    public boolean shouldShowPermissionRationale(Permission permissionType) {
        return getView() != null && getView().shouldShowPermissionRationale(permissionType);
    }

    private void requestPermissionInternal(Permission permissionType) {
        if (getView() != null) {
            getView().requestPermission(permissionType);
        }
    }

    public void showPermissionRationaleDialog(Permission permissionType) {
        if (getView() != null) {
            getView().showPermissionRationaleDialog(permissionType);
        }
    }

    public void setDialogShowing(boolean showing) {
        dialogShowing = showing;
    }

    public void onRationaleAccepted(Permission permissionType) {
        dialogShowing = false;
        requestPermissionInternal(permissionType);
    }

    public void onRationaleDeclined(Permission permissionType) {
        dialogShowing = false;
    }

    @Override protected void viewDetached() {
    }

    @Override protected void viewAttached() {
    }

    public interface View {

        boolean hasPermission(Permission permissionType);

        boolean shouldShowPermissionRationale(Permission permissionType);

        void requestPermission(Permission permissionType);

        void showPermissionRationaleDialog(Permission permissionType);
    }
}