package com.omahagdg.dagger2demo.dagger;

import android.os.Bundle;

public interface Scoped<T> {

    String getScopeTag();

    T initializeOrGetComponent(Bundle args);
}
