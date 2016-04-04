package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.dagger.ForMain;
import com.omahagdg.dagger2demo.permissions.PermissionRationaleDialog;

import dagger.Subcomponent;

@ForMain
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    void inject(MainFragment mainFragment);

    void inject(PermissionRationaleDialog permissionRationaleDialog);
}
