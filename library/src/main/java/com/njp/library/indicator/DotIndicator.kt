package com.njp.library.indicator

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.njp.library.R

class DotIndicator : Indicator, RelativeLayout {

    private val mAdapter = DotIndicatorAdapter()

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
        val recyclerView = RecyclerView(context)
        layoutParams = layoutParams ?: LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER
        }
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recyclerView.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        recyclerView.adapter = mAdapter

        addView(recyclerView)
        if (attrs != null) {
            initAttrs(attrs)
        }
    }

    private fun initAttrs(attrs: AttributeSet) {

    }


    override fun onPageSelected(position: Int) {
        mAdapter.setActiveIndex(position)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float) {

    }

    override fun onCountChange(count: Int) {
        mAdapter.setCount(count)
    }


    private class DotIndicatorAdapter : RecyclerView.Adapter<DotIndicatorAdapter.ViewHolder>() {

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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ImageView(parent.context).apply {
                layoutParams = LinearLayout.LayoutParams(40, 40).apply {
                    marginStart = 10
                    marginEnd = 10
                }
                setImageResource(R.drawable.circle)
            })
        }

        override fun getItemCount(): Int {
            return mCount
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.circle.setColor(if (position == mActiveIndex) Color.BLACK else Color.GRAY)
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val circle = (itemView as ImageView).drawable as GradientDrawable
        }
    }

}