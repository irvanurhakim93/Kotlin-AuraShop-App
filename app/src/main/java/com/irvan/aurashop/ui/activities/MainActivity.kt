package com.irvan.aurashop.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences(Constant.APP_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val logged_in_username = sharedPreferences.getString(Constant.LOGGED_IN_USERNAME,"")!!

        binding.tvMainActivity.text = "Hai $logged_in_username"
    }
}