package com.irvan.aurashop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AppFontLight (context: Context, attrs:AttributeSet): AppCompatTextView(context, attrs){

    init {
        applyFont()
    }

    private fun applyFont(){
        val typeface : Typeface = Typeface.createFromAsset(context.assets, "app_font_light.ttf")
        setTypeface(typeface)
    }

}