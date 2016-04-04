package com.omahagdg.dagger2demo.base;

import android.support.annotation.Nullable;

public abstract class Presenter<V> {

    private V view;

    public void takeView(V view) {
        if (view == null) throw new NullPointerException("New view must not be null");

        if (this.view != view) {
            if (this.view != null) dropView(this.view);

            this.view = view;
            viewAttached();
        }
    }

    public void dropView(V view) {
        if (view == null) throw new NullPointerException("Dropped view must not be null");
        if (this.view == view) {
            this.view = null;
            viewDetached();
        }
    }

    @Nullable protected V getView() {
        return view;
    }

    protected abstract void viewAttached();

    protected abstract void viewDetached();
}
