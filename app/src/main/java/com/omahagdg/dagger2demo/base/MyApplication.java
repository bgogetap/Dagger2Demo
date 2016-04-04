package com.omahagdg.dagger2demo.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.omahagdg.dagger2demo.dagger.ComponentCache;

import javax.inject.Inject;

public class MyApplication extends Application {

    @Inject ComponentCache componentCache;

    private MyApplicationComponent component = createComponent();

    @Override public void onCreate() {
        super.onCreate();
        component.inject(this);
        Stetho.initializeWithDefaults(this);
    }

    @Override public Object getSystemService(String name) {
        if (name.equals(ComponentCache.SERVICE_NAME)) {
            return componentCache;
        }
        if (componentCache.hasComponentForTag(name)) {
            return componentCache.getComponentForTag(name);
        }
        return super.getSystemService(name);
    }

    protected MyApplicationComponent createComponent() {
        return DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .build();
    }

    public MyApplicationComponent getComponent() {
        return component;
    }
}
