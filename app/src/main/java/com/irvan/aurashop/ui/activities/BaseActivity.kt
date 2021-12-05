package com.irvan.aurashop.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.irvan.aurashop.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snackBarError))
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.snackBarSuccess))
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String){

        mProgressDialog = Dialog(this)

        //Mengatur layar kontent / isi dari sumber layout

        mProgressDialog.setContentView(R.layout.dialog_progress_bar)

        mProgressDialog.findViewById<TextView>(R.id.progressTitle).text = text

        mProgressDialog.setCancelable(false)

        mProgressDialog.setCanceledOnTouchOutside(false)

        //Menampilkan dialog dan memunculkannya pada layar
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }


    fun doubleBackToExit(){

        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this,resources.getString(R.string.please_click_back_to_exit_msg),Toast.LENGTH_SHORT).show()

        //Menerapkan tombol back dari versi terdahulu yang mungkin sudah deprecated atau sudah usang
        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }
}


