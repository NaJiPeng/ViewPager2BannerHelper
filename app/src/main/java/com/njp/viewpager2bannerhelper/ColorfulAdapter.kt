package com.njp.viewpager2bannerhelper

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.njp.library.loop.LoopAdapter

class ColorfulAdapter : LoopAdapter<ColorfulAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView as TextView
    }

    override fun getRealItemCount(): Int {
        return 4
    }

    override fun onBindRealViewHolder(holder: ViewHolder, realPosition: Int) {
        holder.textView.apply {
            setText("Fragment $realPosition")
            setBackgroundColor(
                when (realPosition) {
                    0 -> Color.parseColor("#4CAF50")
                    1 -> Color.parseColor("#D81B60")
                    2 -> Color.parseColor("#2196F3")
                    else -> Color.parseColor("#FF9800")
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        })
    }
}