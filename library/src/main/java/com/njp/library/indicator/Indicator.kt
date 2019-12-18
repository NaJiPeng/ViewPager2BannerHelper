package com.njp.library.indicator

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
     * 处理无限循环的情况
     */
    fun setupWithViewPager2(viewPager2: ViewPager2) {

        //TODO
        //为了监听Adapter变动和数据个数变动
        viewPager2.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            setupWithViewPager2(viewPager2)
        }

        val adapter = viewPager2.adapter

        //初始化个数
        onCountChange(if (adapter is Countable) adapter.getRealItemCount() else adapter?.itemCount ?: 0)

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
    }

}