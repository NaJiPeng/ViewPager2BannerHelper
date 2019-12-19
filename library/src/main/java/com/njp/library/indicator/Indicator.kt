package com.njp.library.indicator

import androidx.core.view.doOnNextLayout
import androidx.viewpager2.widget.ViewPager2
import com.njp.library.adapter.Countable

/**
 * Indicator抽象接口
 */
interface Indicator {

    fun onPageSelected(position: Int)

    fun onPageScrolled(position: Int, positionOffset: Float)

    fun onCountChange(count: Int)

    /**
     * 处理无限循环的情况，设置监听，初始化
     */
    fun setupWithViewPager2(viewPager2: ViewPager2) {

        //为了监听Adapter变动和数据个数变动
        viewPager2.doOnNextLayout {
            setupWithViewPager2(viewPager2)
        }

        val adapter = viewPager2.adapter

        viewPager2.setPageTransformer { v, p ->

        }

        //初始化个数
        onCountChange(
            if (adapter is Countable) adapter.getRealItemCount() else adapter?.itemCount ?: 0
        )

        //设置回调
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (adapter is Countable) {
                    this@Indicator.onPageSelected(position % adapter.getRealItemCount())
                } else {
                    this@Indicator.onPageSelected(position)
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (adapter is Countable) {
                    this@Indicator.onPageScrolled(
                        position % adapter.getRealItemCount(),
                        positionOffset
                    )
                } else {
                    this@Indicator.onPageScrolled(position, positionOffset)
                }
            }
        })

        //设置当前Indicator的选中项
        val currentIndex =
            if (adapter is Countable) viewPager2.currentItem % adapter.getRealItemCount()
            else viewPager2.currentItem
        onPageSelected(currentIndex)
    }
}