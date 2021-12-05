package com.irvan.aurashop.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.google.firebase.auth.FirebaseAuth
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityLoginBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.User
import com.irvan.aurashop.utils.Constant

class LoginActivity : BaseActivity(), View.OnClickListener {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            binding.loginActivityForgetPw.setOnClickListener(this)
            binding.loginActivityButton.setOnClickListener(this)
            binding.loginActivityRegisterBtn.setOnClickListener(this)

        }



    }

     fun userLoggedInSuccess(user: User){
        //Menyembunyikan progress dialog
        hideProgressDialog()

        //Jika user.profilecompleted nilainya 0 maka user yang baru registrasi sebelum login akan diarahkan menuju kelas activity lain guna melengkapi detail identitas sang user tersebut
        if (user.profileCompleted == 0){
            val completeIntent = Intent(this@LoginActivity, CompleteProfileUserActivity::class.java)
            completeIntent.putExtra(Constant.EXTRA_USER_DETAILS, user)
            startActivity(completeIntent)
        } else {
            //mengarahkan pengguna ke halaman awal setelah login
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

    }

    //dalam tampilan login ini terdapat beberapa komponen yang clickable atau yang bisa di klik,seperti lupa password dan teks buat akun
    override fun onClick(view: View?){
        if (view != null){
            when(view.id){
                R.id.login_activity_forget_pw -> {
                    val ForgetPassIntent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(ForgetPassIntent)
                }

                R.id.login_activity_button -> {
                    loginRegisteredUser()

                }

                R.id.login_activity_register_btn -> {
                    //Menjalankan tampilan pendaftaran ketika user mengklik teks tersebut
                    val RegisterIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(RegisterIntent)
                }

            }
        }
    }

     private fun validateLoginDetails(): Boolean{
         return when {
             TextUtils.isEmpty(binding.loginActivityEmail.text.toString().trim { it <=' ' }) -> {
                 showErrorSnackBar(resources.getString(R.string.error_input_email),true)
                 false
             }
             TextUtils.isEmpty(binding.loginActivityPassword.text.toString().trim { it <= ' ' }) -> {
                 showErrorSnackBar(resources.getString(R.string.error_input_password), true)
                 false
             }
             else -> {
                 true
             }
         }
     }
        private fun loginRegisteredUser(){
            if (validateLoginDetails()){
                //memunculkan progress dialog
                showProgressDialog(resources.getString(R.string.please_wait_msg))

                //Mendapatkan text dari edit text dan trim
                val email = binding.loginActivityEmail.text.toString().trim { it <= ' ' }
                val password = binding.loginActivityPassword.text.toString().trim { it <= ' ' }
                //login dengan FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                             FirestoreClass().getUserDetails(this@LoginActivity)
                            } else {
                                hideProgressDialog()
                                showErrorSnackBar(task.exception!!.message.toString(), true)
                            }
                        }
            }
        }






}