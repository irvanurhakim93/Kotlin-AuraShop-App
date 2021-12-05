package com.irvan.aurashop.ui.activities

import android.graphics.fonts.Font
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivitySettingsBinding

class SeeDetailsBillsActivity : AppCompatActivity() {

    val PADDING_EDGE = 40f
    val TEXT_TOP_PADDING = 3f
    val TABLE_TOP_PADDING = 10f
    val TEXT_TOP_PADDING_EXTRA = 30f
    val BILL_DETAILS_TOP_PADDING = 80f


    private lateinit var binding:ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}