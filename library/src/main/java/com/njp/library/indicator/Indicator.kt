package com.njp.library.indicator

import androidx.viewpager2.widget.ViewPager2

/**
 * Indicator抽象接口
 */
interface Indicator {

    fun onPageSelected(position: Int)

    fun onPageScrolled(position: Int, positionOffset: Float)

    fun onCountChange(count: Int)

    fun setupWithViewPager2(viewPager2: ViewPager2)

}