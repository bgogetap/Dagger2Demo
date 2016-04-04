package com.omahagdg.dagger2demo;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.omahagdg.dagger2demo.base.MockMyApplication;

public class MockTestRunner extends AndroidJUnitRunner {

    @Override public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, MockMyApplication.class.getName(), context);
    }
}
