package com.njp.viewpager2bannerhelper

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ColorfulFragment : Fragment() {

    private lateinit var textView: TextView
    private var color = 0
    private var text = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_fragment, container)
        textView = view.findViewById<TextView>(R.id.textView)
        textView.setBackgroundColor(color)
        textView.text = text
        return view
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun setText(text: String) {
        this.text = text
    }

}