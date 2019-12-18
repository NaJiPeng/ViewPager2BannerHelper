package com.njp.library.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.njp.library.R
import com.njp.library.decoration.CenterItemDecoration
import com.njp.library.decoration.MarginItemDecoration

/**
 * ImageView作为指示器
 */
class DrawableIndicator : Indicator, RelativeLayout {

    private val mAdapter = DrawableIndicatorAdapter()
    private val mCenterItemDecoration = CenterItemDecoration()
    private val mMarginItemDecoration = MarginItemDecoration(
        resources.getDimensionPixelSize(R.dimen.item_margin)
    )
    private var mItemWidth = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mItemHeight = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mActiveItemWidth = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mActiveItemHeight = resources.getDimensionPixelSize(R.dimen.item_size)
    private var mItemDrawableResource = R.drawable.default_item
    private var mActiveItemDrawableResource = R.drawable.active_item

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

    private fun initialize(context: Context, attrs: AttributeSet?) {
        layoutParams = layoutParams ?: LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        gravity = Gravity.CENTER
        val recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            adapter = mAdapter
            addItemDecoration(mCenterItemDecoration)
            addItemDecoration(mMarginItemDecoration)
        }

        addView(recyclerView)
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
            mActiveItemWidth = getDimensionPixelSize(
                R.styleable.DrawableIndicator_active_item_width,
                mItemWidth
            )
            mActiveItemHeight = getDimensionPixelSize(
                R.styleable.DrawableIndicator_active_item_height,
                mItemHeight
            )
            mMarginItemDecoration.margin = getDimensionPixelSize(
                R.styleable.DrawableIndicator_item_margin,
                resources.getDimensionPixelSize(R.dimen.item_margin)
            )
            mItemDrawableResource = getResourceId(
                R.styleable.DrawableIndicator_item_drawable,
                R.drawable.default_item
            )
            mActiveItemDrawableResource = getResourceId(
                R.styleable.DrawableIndicator_active_item_drawable,
                R.drawable.active_item
            )
            recycle()
        }
    }

    fun setItemWidth(itemWidth: Int) = apply {
        this.mItemWidth = itemWidth
    }

    fun setItemHeight(itemHeight: Int) = apply {
        this.mItemHeight = itemHeight
    }

    fun setActiveItemWidth(activeItemWidth: Int) = apply {
        this.mActiveItemWidth = activeItemWidth
    }

    fun setActiveItemHeight(activeItemHeight: Int) = apply {
        this.mActiveItemHeight = activeItemHeight
    }

    fun setItemMargin(itemMargin: Int) = apply {
        this.mMarginItemDecoration.margin = itemMargin
    }

    fun setItemDrawableResource(drawableResource: Int) = apply {
        this.mItemDrawableResource = drawableResource
    }

    fun setActiveItemDrawableResource(activeDrawableResource: Int) = apply {
        this.mActiveItemDrawableResource = activeDrawableResource
    }

    override fun onPageSelected(position: Int) {
        mAdapter.setActiveIndex(position)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float) {
        //DO NOTHING
    }

    override fun onCountChange(count: Int) {
        mAdapter.setCount(count)
    }

    /**
     * 拦截触摸事件，防止影响下层ViewPager2滑动
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    /**
     * 作为Indicator的RecyclerView的Adapter
     */
    private inner class DrawableIndicatorAdapter :
        RecyclerView.Adapter<ViewHolder>() {

        private var mActiveIndex = 0
        private var mCount = 0

        fun setCount(count: Int) {
            mCount = count
            notifyDataSetChanged()
        }

        fun setActiveIndex(activeIndex: Int) {
            mActiveIndex = activeIndex
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int) = if (mActiveIndex == position) 1 else 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ImageView(parent.context).apply {
                    layoutParams = LayoutParams(
                        if (viewType == 1) mActiveItemWidth else mItemWidth,
                        if (viewType == 1) mActiveItemHeight else mItemHeight
                    )
                }
            )
        }

        override fun getItemCount() = mCount

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageResource(
                if (position == mActiveIndex) mActiveItemDrawableResource else mItemDrawableResource
            )
        }

    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView as ImageView
    }
}
