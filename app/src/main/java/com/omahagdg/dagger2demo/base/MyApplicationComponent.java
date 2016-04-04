package com.omahagdg.dagger2demo.base;

import com.omahagdg.dagger2demo.forecast.ForecastComponent;
import com.omahagdg.dagger2demo.forecast.ForecastModule;
import com.omahagdg.dagger2demo.main.MainComponent;
import com.omahagdg.dagger2demo.main.MainModule;
import com.omahagdg.dagger2demo.permissions.PermissionRationaleDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MyApplicationModule.class)
public interface MyApplicationComponent {

    MainComponent plus(MainModule mainModule);

    ForecastComponent plus(ForecastModule forecastModule);

    void inject(MyApplication application);

    void inject(MainActivity mainActivity);

    void inject(PermissionRationaleDialog permissionRationaleDialog);
}
