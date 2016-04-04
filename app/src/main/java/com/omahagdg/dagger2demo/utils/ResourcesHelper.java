package com.omahagdg.dagger2demo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;

@SuppressWarnings("deprecation") public class ResourcesHelper {

    private ResourcesHelper() {

    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(drawableRes);
        } else {
            return context.getDrawable(drawableRes);
        }
    }

    public static int getColor(Context context, int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorRes);
        } else {
            return context.getColor(colorRes);
        }
    }
}
