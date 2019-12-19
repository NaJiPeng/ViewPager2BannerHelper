package com.njp.library.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.njp.library.R
import com.njp.library.adapter.Countable

/**
 * ImageView作为指示器
 */
class DrawableIndicator : Indicator, LinearLayout {

    private var mItemCount = 0
    private var mSelectedIndex = -1
    private var mItemWidth = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mItemHeight = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mSelectedItemWidth = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mSelectedItemHeight = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mItemMargin = resources.getDimensionPixelSize(R.dimen.item_margin)
    private var mItemDrawableResource = R.drawable.default_item
    private var mSelectedItemDrawableResource = R.drawable.active_item
    private var mIndicatorTransformer: IndicatorTransformer? = null

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            this@DrawableIndicator.onPageSelected(position % mItemCount)

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@DrawableIndicator.onPageScrolled(position % mItemCount, positionOffset)
        }
    }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    /**
     * 初始化
     */
    private fun initialize(context: Context, attrs: AttributeSet?) {
        layoutParams = layoutParams ?: LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        gravity = Gravity.CENTER

        if (attrs != null) {
            initAttrs(context, attrs)
        }
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.DrawableIndicator).apply {

            mItemWidth = getDimensionPixelSize(
                R.styleable.DrawableIndicator_item_width,
                resources.getDimensionPixelSize(R.dimen.item_size)
            )
            mItemHeight = getDimensionPixelSize(
                R.styleable.DrawableIndicator_item_height,
                resources.getDimensionPixelSize(R.dimen.item_size)
            )
            mSelectedItemWidth = getDimensionPixelSize(
                R.styleable.DrawableIndicator_selected_item_width,
                mItemWidth
            )
            mSelectedItemHeight = getDimensionPixelSize(
                R.styleable.DrawableIndicator_selected_item_height,
                mItemHeight
            )
            mItemMargin = getDimensionPixelSize(
                R.styleable.DrawableIndicator_item_margin,
                resources.getDimensionPixelSize(R.dimen.item_margin)
            )
            mItemDrawableResource = getResourceId(
                R.styleable.DrawableIndicator_item_drawable,
                R.drawable.default_item
            )
            mSelectedItemDrawableResource = getResourceId(
                R.styleable.DrawableIndicator_selected_item_drawable,
                R.drawable.active_item
            )
            recycle()
        }
    }

    /**
     * 初始化indicators
     */
    private fun initIndicators() {
        removeAllViews()
        for (i in 0 until mItemCount) {
            val margin = mItemMargin / 2
            val horizontalMargin = if (orientation == HORIZONTAL) margin else 0
            val verticalMargin = if (orientation == VERTICAL) margin else 0

            addView(ImageView(context).apply {
                layoutParams = LayoutParams(mItemWidth, mItemWidth).apply {
                    setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
                }
                setImageResource(mItemDrawableResource)
            })
        }

    }

    /**
     * 刷新视图状态
     */
    private fun refresh() {
        forEachIndexed { index, view ->
            val width = if (index == mSelectedIndex) mSelectedItemWidth else mItemWidth
            val height = if (index == mSelectedIndex) mSelectedItemHeight else mItemHeight

            val margin = mItemMargin / 2
            val horizontalMargin = if (orientation == HORIZONTAL) margin else 0
            val verticalMargin = if (orientation == VERTICAL) margin else 0

            val drawableResource =
                if (index == mSelectedIndex) mSelectedItemDrawableResource else mItemDrawableResource

            (view as ImageView).apply {
                layoutParams = (view.layoutParams as LayoutParams).apply {
                    this.width = width
                    this.height = height

                    setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
                }
                setImageResource(drawableResource)
            }
        }
    }

    fun setItemWidth(itemWidth: Int) {
        this.mItemWidth = itemWidth
        refresh()
    }

    fun setItemHeight(itemHeight: Int) {
        this.mItemHeight = itemHeight
        refresh()
    }

    fun setSelectedItemWidth(selectedItemWidth: Int) {
        this.mSelectedItemWidth = selectedItemWidth
        refresh()
    }

    fun setSelectedItemHeight(selectedItemHeight: Int) {
        this.mSelectedItemHeight = selectedItemHeight
        refresh()
    }

    fun setItemMargin(itemMargin: Int) {
        this.mItemMargin = itemMargin
        refresh()
    }

    fun setItemDrawableResource(drawableResource: Int) {
        this.mItemDrawableResource = drawableResource
        refresh()
    }

    fun setSelectedItemDrawableResource(selectedDrawableResource: Int) {
        this.mSelectedItemDrawableResource = selectedDrawableResource
        refresh()
    }

    fun setIndicatorTransformer(indicatorTransformer: IndicatorTransformer) {
        this.mIndicatorTransformer = indicatorTransformer
    }


    fun setIndicatorTransformer(indicatorTransformer: (indicator: View, offset: Float) -> Unit) =
        apply {
            this.mIndicatorTransformer = object : IndicatorTransformer {
                override fun transformIndicator(indicator: View, offset: Float) {
                    indicatorTransformer.invoke(indicator, offset)
                }

            }
        }


    override fun setupWithViewPager2(viewPager2: ViewPager2) {

        //为了监听Adapter变动和数据个数变动
        viewPager2.doOnNextLayout {
            setupWithViewPager2(viewPager2)
        }

        val adapter = viewPager2.adapter

        //初始化个数
        onCountChange(
            if (adapter is Countable) adapter.getRealItemCount() else adapter?.itemCount ?: 0
        )

        viewPager2.unregisterOnPageChangeCallback(callback)
        viewPager2.registerOnPageChangeCallback(callback)

        //设置当前Indicator的选中项
        onPageSelected(viewPager2.currentItem % mItemCount)
    }

    override fun onCountChange(count: Int) {
        if (count != mItemCount) {
            this.mItemCount = count
            initIndicators()
        }
    }

    override fun onPageSelected(position: Int) {
        if (position != mSelectedIndex) {
            mSelectedIndex = position
            refresh()
        }
    }

    /**
     * 0表示未选中状态，1表示选中状态
     * 以下为从第二页转到第三页的回调状态：
     *
     * 0.0——1.0——0.0——0.0
     * 0.0——0.8——0.2——0.0
     * 0.0——0.5——0.5——0.0
     * 0.0——0.3——0.7——0.0
     * 0.0——0.0——1.0——0.0
     */
    override fun onPageScrolled(position: Int, positionOffset: Float) {
        getChildAt(position)?.let {
            mIndicatorTransformer?.transformIndicator(it, 1 - positionOffset)
        }
        getChildAt((position + 1) % mItemCount)?.let {
            mIndicatorTransformer?.transformIndicator(it, positionOffset)
        }
    }

}
