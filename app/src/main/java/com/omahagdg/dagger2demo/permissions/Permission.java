package com.omahagdg.dagger2demo.permissions;

import com.omahagdg.dagger2demo.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public enum Permission {

    LOCATION(1, ACCESS_FINE_LOCATION, R.string.location_rationale_title, R.string.location_rationale_message),
    INVALID(-1, "", -1, -1);

    public final int id;
    public final String permissionString;
    public final int rationaleTitle;
    public final int rationaleMessage;

    Permission(int id, String permissionString, int rationaleTitle, int rationaleMessage) {
        this.id = id;
        this.permissionString = permissionString;
        this.rationaleTitle = rationaleTitle;
        this.rationaleMessage = rationaleMessage;
    }

    public static Permission fromId(int id) {
        for (Permission permission : values()) {
            if (permission.id == id) {
                return permission;
            }
        }
        return INVALID;
    }
}
