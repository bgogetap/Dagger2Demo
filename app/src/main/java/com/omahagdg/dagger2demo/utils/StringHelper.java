package com.omahagdg.dagger2demo.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class StringHelper {

    private final Context context;

    @Inject public StringHelper(Context context) {
        this.context = context;
    }

    public String getString(@StringRes int stringRes) {
        return context.getString(stringRes);
    }

    public String getString(@StringRes int stringRes, Object... formatArgs) {
        return context.getString(stringRes, formatArgs);
    }

    public String[] getStringArray(@ArrayRes int arrayRes) {
        return context.getResources().getStringArray(arrayRes);
    }
}
