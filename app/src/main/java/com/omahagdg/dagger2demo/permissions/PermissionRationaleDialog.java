package com.omahagdg.dagger2demo.permissions;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;

import com.omahagdg.dagger2demo.base.MyApplication;

import javax.inject.Inject;

public class PermissionRationaleDialog extends DialogFragment {

    @Inject PermissionHelper permissionHelper;

    public static PermissionRationaleDialog newInstance(Permission permissionType) {
        PermissionRationaleDialog dialog = new PermissionRationaleDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("permission_id", permissionType.id);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((MyApplication) getContext().getApplicationContext()).getComponent().inject(this);
        Permission permission = Permission.fromId(getArguments().getInt("permission_id"));
        return new Builder(getContext())
                .setTitle(permission.rationaleTitle)
                .setMessage(permission.rationaleMessage)
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                    permissionHelper.onRationaleAccepted(permission);
                    dismiss();
                })
                .setNegativeButton(getString(android.R.string.cancel), ((dialog1, which1) -> {
                    permissionHelper.onRationaleDeclined(permission);
                    dismiss();
                })).create();
    }
}
