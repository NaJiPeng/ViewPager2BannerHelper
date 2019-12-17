package com.njp.viewpager2bannerhelper

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.njp.library.adapter.LoopFragmentAdapter

class ColorfulFragmentAdapter(fragmentActivity: FragmentActivity) :
    LoopFragmentAdapter(fragmentActivity) {

    override fun getRealItemCount(): Int {
        return 4
    }

    override fun createRealFragment(position: Int): Fragment {
        return ColorfulFragment().apply {
            setText("Fragment $position")
            setColor(
                when (position) {
                    0 -> Color.parseColor("#4CAF50")
                    1 -> Color.parseColor("#D81B60")
                    2 -> Color.parseColor("#2196F3")
                    else -> Color.parseColor("#FF9800")
                }
            )
        }
    }


}