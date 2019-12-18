package com.njp.viewpager2bannerhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.njp.library.controller.AutoPlayController
import com.njp.library.controller.AutoPlayMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2.adapter = ImageAdapter(
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

        dotIndicator.setupWithViewPager2(viewPager2)


    }
}
