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
