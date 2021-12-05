package com.irvan.aurashop.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
        val user_id:String = "",
        val user_name:String ="",
        val product_name:String="",
        val product_price:String="",
        val product_description:String="",
        val product_quantity:String="",
        val product_image:String="",
        var product_id:String ="",): Parcelable