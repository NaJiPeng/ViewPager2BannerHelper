package com.njp.library.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * RecyclerView实现副轴方向上居中效果的Decoration
 */
class CenterItemDecoration : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val linearLayoutManager = requireLinearLayoutManager(parent)
        if (linearLayoutManager.orientation == LinearLayoutManager.VERTICAL) {
            val gap = parent.width - view.width
            if (gap > 0) {
                outRect.left = gap / 2
                outRect.right = gap / 2
            }
        } else {
            val gap = parent.height - view.height
            if (gap > 0) {
                outRect.top = gap / 2
                outRect.bottom = gap / 2
            }
        }
    }

    private fun requireLinearLayoutManager(parent: RecyclerView): LinearLayoutManager {
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            return layoutManager
        }
        throw IllegalStateException("The layoutManager must be LinearLayoutManager")
    }

}