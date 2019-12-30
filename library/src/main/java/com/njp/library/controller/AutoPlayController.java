package com.njp.library.controller;


import android.os.Handler;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class AutoPlayController implements LifecycleObserver {

    public static final int MODE_NONE = -1;
    public static final int MODE_ACTIVE = 0;
    public static final int MODE_VISIBLE = 1;
    public static final int MODE_WHOLE = 2;

    @Retention(SOURCE)
    @IntDef({MODE_ACTIVE, MODE_VISIBLE, MODE_WHOLE, MODE_NONE})
    public @interface AutoPlayMode {
    }

    private final Handler mHandler = new Handler();

    private final Runnable mRunnable = this::increaseIndex;

    private boolean autoPlay = true;

    private long mInterval = 2000L;

    private ViewPager2 mViewPager2 = null;

    @AutoPlayMode
    private int mAutoPlayMode = MODE_NONE;

    private ViewPager2.OnPageChangeCallback mCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager2.SCROLL_STATE_DRAGGING:
                    stopPlay();
                    break;
                case ViewPager2.SCROLL_STATE_IDLE:
                    startPlay();
                    break;
                case ViewPager2.SCROLL_STATE_SETTLING:
                    break;
            }
        }
    };

    private View.OnAttachStateChangeListener mListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            mViewPager2 = null;
        }
    };

    public AutoPlayController enableAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }

    public AutoPlayController setInterval(long interval) {
        this.mInterval = interval;
        return this;
    }

    public AutoPlayController setViewPager2(@NotNull ViewPager2 viewPager2) {
        if (this.mViewPager2 != viewPager2) {
            if (this.mViewPager2 != null) {
                this.mViewPager2.unregisterOnPageChangeCallback(mCallback);
                this.mViewPager2.removeOnAttachStateChangeListener(mListener);
            }

            this.mViewPager2 = viewPager2;

            this.mViewPager2.registerOnPageChangeCallback(mCallback);
            this.mViewPager2.addOnAttachStateChangeListener(mListener);
        }
        return this;
    }

    public AutoPlayController setLifecycle(@NotNull Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }

    public AutoPlayController setAutoPlayMode(@AutoPlayMode int autoPlayMode) {
        this.mAutoPlayMode = autoPlayMode;
        return this;
    }

    public void startAutoPlay() {
        this.autoPlay = true;
        startPlay();
    }

    public void stopAutoPlay() {
        autoPlay = false;
        stopPlay();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        if (MODE_WHOLE == mAutoPlayMode) {
            startPlay();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart() {
        if (MODE_VISIBLE == mAutoPlayMode) {
            startPlay();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if (MODE_ACTIVE == mAutoPlayMode) {
            startPlay();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if (MODE_ACTIVE == mAutoPlayMode) {
            stopPlay();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop() {
        if (MODE_VISIBLE == mAutoPlayMode) {
            stopPlay();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        stopPlay();
        mViewPager2 = null;
    }

    private void increaseIndex() {
        if (mViewPager2 != null) {
            mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
        }
    }

    private void startPlay() {
        if (autoPlay) {
            mHandler.postDelayed(mRunnable, mInterval);
        }
    }

    private void stopPlay() {
        mHandler.removeCallbacks(mRunnable);
    }
}
