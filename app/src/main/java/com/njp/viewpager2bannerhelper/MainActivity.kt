package com.njp.viewpager2bannerhelper

import android.animation.Animator
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.VectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.njp.library.controller.AutoPlayController
import com.njp.library.controller.AutoPlayMode
import com.njp.library.indicator.IndicatorTransformer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2.adapter = MyAdapter(
            listOf(
                "https://th.wallhaven.cc/lg/vg/vg3wm5.jpg",
                "https://th.wallhaven.cc/lg/5w/5w6j85.jpg",
                "https://th.wallhaven.cc/lg/5w/5w6319.jpg",
                "https://th.wallhaven.cc/lg/ym/ymwggg.jpg"
            )
        )

        viewPager2.offscreenPageLimit = 1


        AutoPlayController()
            .enableAutoPlay(true)
            .setAutoPlayMode(AutoPlayMode.VISIBLE)
            .setInterval(2000)
            .setLifecycleOwner(this)
            .setViewPager2(viewPager2)

        indicator.setupWithViewPager2(viewPager2)
        indicator.setIndicatorTransformer { indicator, offset ->
            val vectorDrawable = (indicator as ImageView).drawable as VectorDrawable
        }


    }
}
