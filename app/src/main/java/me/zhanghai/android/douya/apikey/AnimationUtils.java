/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

public class AnimationUtils {

    private AnimationUtils() {}

    public static void fadeOut(final View view, int duration, final boolean gone,
                               final Runnable nextRunnable) {
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() == 0) {
            // Cancel any starting animation.
            view.animate()
                    .alpha(0)
                    .setDuration(0)
                    .start();
            view.setVisibility(gone ? View.GONE : View.INVISIBLE);
            if (nextRunnable != null) {
                nextRunnable.run();
            }
            return;
        }
        view.animate()
                .alpha(0)
                .setDuration(duration)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    private boolean mCanceled = false;
                    @Override
                    public void onAnimationCancel(Animator animator) {
                        mCanceled = true;
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!mCanceled) {
                            view.setVisibility(gone ? View.GONE : View.INVISIBLE);
                            if (nextRunnable != null) {
                                nextRunnable.run();
                            }
                        }
                    }
                })
                .start();
    }

    public static void fadeOut(View view, int duration, boolean gone) {
        fadeOut(view, duration, gone, null);
    }

    public static void fadeOut(View view, boolean gone) {
        fadeOut(view, getShortAnimTime(view), gone);
    }

    public static void fadeOut(View view) {
        fadeOut(view, true);
    }

    public static void fadeIn(View view, int duration) {
        if (view.getVisibility() == View.VISIBLE && view.getAlpha() == 1) {
            // Cancel any starting animation.
            view.animate()
                    .alpha(1)
                    .setDuration(0)
                    .start();
            return;
        }
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator())
                // NOTE: We need to remove any previously set listener or Android will reuse it.
                .setListener(null)
                .start();
    }

    public static void fadeIn(View view) {
        fadeIn(view, getShortAnimTime(view));
    }

    public static void fadeToVisibility(View view, boolean visible, boolean gone) {
        if (visible) {
            fadeIn(view);
        } else {
            fadeOut(view, gone);
        }
    }

    public static void fadeToVisibility(View view, boolean visible) {
        fadeToVisibility(view, visible, true);
    }

    public static void crossfade(View fromView, View toView, int duration, boolean gone) {
        fadeOut(fromView, duration, gone);
        fadeIn(toView, duration);
    }

    public static void crossfade(View fromView, View toView, boolean gone) {
        crossfade(fromView, toView, getMediumAnimTime(fromView), gone);
    }

    public static void crossfade(View fromView, View toView) {
        crossfade(fromView, toView, true);
    }

    public static void fadeOutThenFadeIn(final View fromView, final View toView, final int duration,
                                         final boolean gone) {
        fadeOut(fromView, duration, gone, new Runnable() {
            @Override
            public void run() {
                fadeIn(toView, duration);
            }
        });
    }

    public static void fadeOutThenFadeIn(View fromView, View toView, boolean gone) {
        fadeOutThenFadeIn(fromView, toView, getShortAnimTime(fromView), gone);
    }

    public static void fadeOutThenFadeIn(final View fromView, final View toView) {
        fadeOutThenFadeIn(fromView, toView, true);
    }

    public static int getShortAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_shortAnimTime);
    }

    public static int getShortAnimTime(View view) {
        return getShortAnimTime(view.getResources());
    }

    public static int getShortAnimTime(Context context) {
        return getShortAnimTime(context.getResources());
    }

    public static int getMediumAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_mediumAnimTime);
    }

    public static int getMediumAnimTime(View view) {
        return getMediumAnimTime(view.getResources());
    }

    public static int getMediumAnimTime(Context context) {
        return getMediumAnimTime(context.getResources());
    }

    public static int getLongAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_longAnimTime);
    }

    public static int getLongAnimTime(View view) {
        return getLongAnimTime(view.getResources());
    }

    public static int getLongAnimTime(Context context) {
        return getLongAnimTime(context.getResources());
    }
}
