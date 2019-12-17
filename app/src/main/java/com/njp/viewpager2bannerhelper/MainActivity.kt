package com.njp.viewpager2bannerhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.njp.library.autoplay.AutoPlayController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2.adapter = ColorfulFragmentAdapter(this)
        AutoPlayController()
            .enableAutoPlay(true)
            .setAutoPlayMode(AutoPlayController.AUTO_PLAY_MODE_VISIBLE)
            .setInterval(2000)
            .setLifecycleOwner(this)
            .setViewPager2(viewPager2)
    }
}
