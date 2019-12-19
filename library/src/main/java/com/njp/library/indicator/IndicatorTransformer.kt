package com.njp.library.indicator

import android.view.View

/**
 * 实现Indicator动画效果的对外接口
 */
@FunctionalInterface
interface IndicatorTransformer {

    fun transformIndicator(indicator: View, offset: Float)

}