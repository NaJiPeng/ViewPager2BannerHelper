package com.njp.viewpager2bannerhelper

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.njp.library.adapter.LoopAdapter

class ImageAdapter(private val mData: List<String>) : LoopAdapter<ImageAdapter.ViewHolder>() {

    override fun getRealItemCount(): Int {
        return mData.size
    }

    override fun onBindRealViewHolder(holder: ViewHolder, realPosition: Int) {
        Glide.with(holder.imageView)
            .load(mData[realPosition])
            .into(holder.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView as ImageView
    }
}
