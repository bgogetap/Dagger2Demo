package com.omahagdg.dagger2demo.dagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface Scoped<T> {

    /**
     * Tag that identifies a given scope. Retrieved from {@link DaggerContextWrapper} which is
     * created when we override {@link Fragment#getContext()} in our fragment subclasses
     */
    String getScopeTag();

    /**
     * Will reuse a component for the current scope if one exists. If not, a new one is created
     * Requiring the Bundle to be passed in here gives us an opportunity to set up our
     * component/module with persisted data in case of process death
     */
    T initializeOrGetComponent(Bundle args);
}
