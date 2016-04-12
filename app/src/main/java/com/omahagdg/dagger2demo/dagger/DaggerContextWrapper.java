package com.omahagdg.dagger2demo.dagger;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

public class DaggerContextWrapper extends ContextWrapper {

    private final String tag;

    private LayoutInflater layoutInflater;

    public DaggerContextWrapper(Context base, String tag) {
        super(base);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * By intercepting calls to the Layout Inflater service, we can ensure that all Contexts that
     * are passed down from our scope root (the Fragments) will be instances of DaggerContextWrapper
     * This means that we can obtain the {@link #getTag()} from, for instance, a View that we
     * manually inflate. Basically, anything with access to Context in a particular scope can be
     * injected with {@link Injector#getComponent(Context)}
     */
    @Override public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return layoutInflater;
        }
        return super.getSystemService(name);
    }
}
