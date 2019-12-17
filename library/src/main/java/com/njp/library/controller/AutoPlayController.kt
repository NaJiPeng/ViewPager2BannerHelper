package com.njp.library.controller

import android.os.Handler
import androidx.annotation.IntDef
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2


/**
 * ViewPager2自动播放的控制器
 */
class AutoPlayController : LifecycleObserver {

    companion object {
        //界面可交互自动播放
        const val AUTO_PLAY_MODE_ACTIVE = 0
        //界面可见自动播放(默认)
        const val AUTO_PLAY_MODE_VISIBLE = 1
        //始终自动播放
        const val AUTO_PLAY_MODE_WHOLE = 2
        //不自动播放
        const val AUTO_PLAY_MODE_NONE = 3

        @IntDef(
            AUTO_PLAY_MODE_ACTIVE,
            AUTO_PLAY_MODE_VISIBLE,
            AUTO_PLAY_MODE_WHOLE,
            AUTO_PLAY_MODE_NONE
        )
        annotation class AutoPlayMode
    }

    private val mHandler = Handler()

    private val mRunnable = Runnable { increaseIndex() }

    private var autoPlay: Boolean = true

    private var mInterval: Long = 3000L

    private var mViewPager2: ViewPager2? = null

    private var mAutoPlayMode = AUTO_PLAY_MODE_ACTIVE

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

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun setAutoPlayMode(@AutoPlayMode autoPlayMode: Int) = apply {
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
        if (AUTO_PLAY_MODE_WHOLE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        if (AUTO_PLAY_MODE_VISIBLE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        if (AUTO_PLAY_MODE_ACTIVE == mAutoPlayMode) {
            startPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        if (AUTO_PLAY_MODE_ACTIVE == mAutoPlayMode) {
            stopPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        if (AUTO_PLAY_MODE_VISIBLE == mAutoPlayMode) {
            stopPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        if (AUTO_PLAY_MODE_WHOLE == mAutoPlayMode) {
            stopPlay()
        }
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