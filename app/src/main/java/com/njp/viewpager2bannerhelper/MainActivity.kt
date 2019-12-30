package com.njp.viewpager2bannerhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.njp.library.controller.AutoPlayController
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
            .setAutoPlayMode(AutoPlayController.MODE_VISIBLE)
            .setInterval(2000)
            .setLifecycle(this.lifecycle)
            .setViewPager2(viewPager2)

        indicator.setupWithViewPager2(viewPager2)
        indicator.setIndicatorTransformer { indicator, offset ->
            indicator.scaleX = 1 + offset * 0.3f
            indicator.scaleY = 1 + offset * 0.3f
        }


    }
}
