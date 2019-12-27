package com.njp.library.controller

import android.os.Handler
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2


/**
 * ViewPager2自动播放的控制器
 */
class AutoPlayController : LifecycleObserver {

    private val mHandler = Handler()

    private val mRunnable = Runnable { increaseIndex() }

    private var autoPlay: Boolean = true

    private var mInterval: Long = 2000L

    private var mViewPager2: ViewPager2? = null

    private var mAutoPlayMode = AutoPlayMode.NONE

    fun enableAutoPlay(autoPlay: Boolean) = apply {
        this.autoPlay = autoPlay
    }

    fun setInterval(interval: Long) = apply {
        this.mInterval = interval
    }

    fun setViewPager2(viewPager2: ViewPager2) = apply {
        this.mViewPager2 = viewPager2
        setupViewPager2(viewPager2)
    }

    fun setLifecycle(lifecycle: Lifecycle) = apply {
        lifecycle.addObserver(this)
    }

    fun setAutoPlayMode(autoPlayMode: AutoPlayMode) = apply {
        this.mAutoPlayMode = autoPlayMode
    }

    fun startAutoPlay() = apply {
        autoPlay = true
        startPlay()
    }

    fun stopAutoPlay() = apply {
        autoPlay = false
        stopPlay()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        if (AutoPlayMode.WHOLE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        if (AutoPlayMode.VISIBLE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        if (AutoPlayMode.ACTIVE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        if (AutoPlayMode.ACTIVE == mAutoPlayMode) {
            stopPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        if (AutoPlayMode.VISIBLE == mAutoPlayMode) {
            stopPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        stopPlay()
        mViewPager2 = null
    }

    /**
     * 在用户手动滑动viewPager2时取消自动滚动，滑动停止时开始自动滚动
     */
    private fun setupViewPager2(viewPager2: ViewPager2) {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> stopPlay()
                    ViewPager2.SCROLL_STATE_IDLE -> startPlay()
                    ViewPager2.SCROLL_STATE_SETTLING -> {//DO NOTHING
                    }
                }
            }
        })

        viewPager2.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                mViewPager2 = null
            }

            override fun onViewAttachedToWindow(v: View?) {
                //DO NOTHING
            }
        })
    }

    private fun increaseIndex() {
        mViewPager2?.let {
            it.currentItem++
        }
    }

    private fun startPlay() {
        if (autoPlay) {
            mHandler.postDelayed(mRunnable, mInterval)
        }
    }

    private fun stopPlay() {
        mHandler.removeCallbacks(mRunnable)
    }

}