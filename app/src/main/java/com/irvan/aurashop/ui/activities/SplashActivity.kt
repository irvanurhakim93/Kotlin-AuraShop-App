package com.irvan.aurashop.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.irvan.aurashop.R
import com.irvan.aurashop.firestore.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            WindowManager.LayoutParams.FLAG_FULLSCREEN
            WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed(
                {

                    // Jika user telah login sekali dan tidak akan logout secara manual dari aplikasi.
                    // Jadi,jika sewaktu waktu user membuka aplikasi akan diarahkan kepada layar utama.
                    // Jika user belum login atau telah logout sebelumnya,maka akan diarahkan kepada halaman login.

                    // Get the current logged in user id
                    val currentUserID = FirestoreClass().getCurrentUserID()

                    if (currentUserID.isNotEmpty()) {
                        // Launch dashboard screen.
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    } else {
                        // Launch the Login Activity
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    finish() // Call this when your activity is done and should be closed.
                },
                2500
        ) // Here we pass the delay time in milliSeconds after which the splash activity will disappear.

    }
}
