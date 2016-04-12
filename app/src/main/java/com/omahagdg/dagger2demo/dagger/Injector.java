package com.omahagdg.dagger2demo.dagger;

import android.content.Context;

import static java.lang.String.format;

@SuppressWarnings("WrongConstant")
public class Injector {

    private Injector() {

    }

    /**
     * Returns the component for the given scope tag. This will fail early if either the context is
     * not an instance of {@link DaggerContextWrapper}, or the component has not been initialized.
     * Call to {@link #checkComponent(Context)} to protect against the latter.
     * @param context Will be verified as instance of {@link DaggerContextWrapper}
     * @param <T> Caller must specify the component type
     */
    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Context context) {
        checkForInstanceOfDaggerContextWrapper(context);
        String tag = ((DaggerContextWrapper) context).getTag();
        T component = (T) context.getApplicationContext().getSystemService(tag);
        if (component == null) {
            throw new NullPointerException(format("Component for tag: '%s' has not been initialized", tag));
        }
        return component;
    }

    public static boolean checkComponent(Context context) {
        checkForInstanceOfDaggerContextWrapper(context);
        String tag = ((DaggerContextWrapper) context).getTag();
        return context.getApplicationContext().getSystemService(tag) != null;
    }

    public static void destroyComponent(Context context) {
        checkForInstanceOfDaggerContextWrapper(context);
        String tag = ((DaggerContextWrapper) context).getTag();
        ComponentCache componentCache = (ComponentCache) context.getApplicationContext()
                .getSystemService(ComponentCache.SERVICE_NAME);
        componentCache.destroyComponentForTag(tag);
    }

    private static void checkForInstanceOfDaggerContextWrapper(Context context) {
        if (!(context instanceof DaggerContextWrapper)) {
            throw new IllegalArgumentException("Context is not instance of DaggerContextWrapper");
        }
    }
}
