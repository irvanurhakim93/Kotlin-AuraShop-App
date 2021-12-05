package com.irvan.aurashop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.irvan.aurashop.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            //memuat foto user pada ImageView
            Glide
                    .with(context)
                    .load(image) //URI dari sebuah foto
                    .centerCrop() //skala dari sebuah foto yang akan dimuat jika foto tersebut dimuat
                    .placeholder(R.drawable.img_placeholder) //sebuah default gambar placeholder jika proses pemuatan gagal
                    .into(imageView)
        } catch (e: IOException){
            e.printStackTrace()
        }
        }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                    .with(context)
                    .load(image) // uri dari sebuah foto
                    .centerCrop() //Tipe skala dari foto
                    .into(imageView) //target view dimana gambar akan dimuat
        } catch (e: IOException){
            e.printStackTrace()
        }

        imageView.setOnClickListener {

        }
    }

    fun loadBillsPicture(image: Any, imageView: ImageView){
        try {
            Glide.with(context)
                    .load(image)
                    .centerCrop()
                    .into(imageView)
        } catch (e:IOException){
            e.printStackTrace()
        }

        imageView.setOnClickListener {

        }
    }

}

