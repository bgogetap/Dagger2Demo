package com.omahagdg.dagger2demo.base;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActivityPresenter extends Presenter<ActivityPresenter.View> {

    @Inject public ActivityPresenter() {
    }

    public void registerActivityResultDelegate(ActivityResultDelegate delegate) {
        if (getView() != null) {
            getView().registerActivityResultDelegate(delegate);
        }
    }

    public void unregisterActivityResultDelegate(ActivityResultDelegate delegate) {
        if (getView() != null) {
            getView().unregisterActivityResultDelegate(delegate);
        }
    }

    @Override protected void viewAttached() {
    }

    @Override protected void viewDetached() {
    }

    public void goToAppSystemSettings() {
        if (getView() != null) {
            getView().goToAppSystemSettings();
        }
    }

    interface View {

        void registerActivityResultDelegate(ActivityResultDelegate delegate);

        void unregisterActivityResultDelegate(ActivityResultDelegate delegate);

        void goToAppSystemSettings();
    }
}
