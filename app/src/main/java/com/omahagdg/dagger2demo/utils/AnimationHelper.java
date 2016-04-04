package com.omahagdg.dagger2demo.utils;

import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AnimationHelper {

    private AnimationHelper() {

    }

    public static void animateViewFromBottom(View viewToAnimate, ViewGroup parentView) {
        parentView.addView(viewToAnimate);
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        viewToAnimate.setLayoutParams(params);

        int height = parentView.getHeight();
        viewToAnimate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int targetY = height / 2 - viewToAnimate.getMeasuredHeight() / 2;

        viewToAnimate.setTranslationY(height);
        viewToAnimate.animate()
                .y(targetY)
                .setInterpolator(new OvershootInterpolator(2F))
                .setDuration(400)
                .start();
    }
}
