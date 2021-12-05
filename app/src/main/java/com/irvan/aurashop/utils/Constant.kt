package com.irvan.aurashop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import java.io.File

object Constant {
    //constant untuk collection USERS
    const val CONFIRMED_PRODUCT:String = "Confirmed_products"
    const val USERS: String = "users"
    const val CART_ITEMS:String = "cart_items"
    const val WISHLIST_ITEMS:String = "wishlist_items"
    const val PRODUCTS : String = "products"
    const val ADDRESSES:String = "addresses"
    const val ORDERS:String = "Orders"
    const val SOLD_PRODUCTS:String = "sold_products"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val APP_PREFERENCE_NAME  = "AuraShopPref"
    //


    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 13
    const val photoFileName = "photo.jpg"

    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    const val PRIA: String = "Pria"
    const val WANITA: String = "Wanita"
    const val FIRST_NAME : String = "firstName"
    const val LAST_NAME : String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE:String = "image"

    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"
    const val COMPLETED_PROFILE: String = "profileCompleted"

    const val EXTRA_PRODUCT_ID: String ="extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID = "extra_product_owner_id"
    const val CART_QUANTITY: String = "cart_quantity"
    const val DEFAULT_CART_QUANTITY:String = "1"

    const val PRODUCT_IMAGE:String = "Product_Image"
    const val PRODUCT_ID:String = "product_id"
    const val STOCK_QUANTITY:String = "stock_quantity"
    const val USER_ID:String = "user_id"

    const val EXTRA_ADDRESS_DETAILS:String = "AddressDetails"
    const val EXTRA_CHECKOUTS:String = "Checkouts"

    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121

    //membuat collection di firestore web
    const val EXTRA_MY_ORDER_DETAILS:String = "extra_my_order_details"

    const val EXTRA_SOLD_PRODUCTS_DETAILS:String = "extra_sold_products_details"


    const val MIDTRANS_BASE_URL = "https://auraparfum.000webhostapp.com/"
    const val MIDTRANS_CLIENT_KEY = "SB-Mid-client-IJW27pVokvswre8-"




    fun showImageChooser(activity: Activity){
        //sebuah intent untuk menjalankan pemilihan foto dalam penyimpanan internal
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //Menjalankan pemilihan gambar dari penyimpanan perangkat menggunakan constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }






    fun getFileExtension(activity: Activity, uri:Uri?): String ?{

        //

        //MimeTypeMap : dua arah map yang memetakan MIME - type kepada ekstensi file dan sebagainya
        //getSingleton(): mendapatkan instance singleton dari MimeTypeMap

        //getSingleton()

        //contoh uri dari file yang tersimpan c://user/irvan/download/gambar.jpg

        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}