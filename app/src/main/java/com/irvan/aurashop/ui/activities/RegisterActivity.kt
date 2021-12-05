package com.irvan.aurashop.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityRegisterBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.User

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            WindowManager.LayoutParams.FLAG_FULLSCREEN
            WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }

        binding.registerActivitySubmitBtn.setOnClickListener {
            registerUser()
        }

    }

    private fun  setupActionBar(){
        setSupportActionBar(binding.registerActivityToolbar)

        val actionBar = supportActionBar
        if(actionBar !=null){
          actionBar.setDisplayHomeAsUpEnabled(true)
          actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.registerActivityToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails(): Boolean{
        return when{
            TextUtils.isEmpty(binding.registerEditTextFirstName.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_first_name),true)
                false
            }

            TextUtils.isEmpty(binding.registerEditTextLastName.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.registerActivityEmail.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_email), true)
                false
            }

            TextUtils.isEmpty(binding.registerActivityPassword.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_password), true)
                false
            }

            TextUtils.isEmpty(binding.registerActivityConfirmPassword.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_confirm_password), true)
                false
            }

            else -> {
//                showErrorSnackBar(resources.getString(R.string.register_success_msg),false)
                true
            }
        }
    }


    private fun registerUser(){
        //Memeriksa fungsi validasi jika inputan apakah valid atau tidak
        if (validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.please_wait_msg))


            val email: String = binding.registerActivityEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.registerActivityPassword.text.toString().trim { it <= ' ' }

            //Membuat instance dan membuat user terdaftar dengan email dan password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{ task ->

                                //Jika registrasi sukses
                                if (task.isSuccessful){

                                //Firebase user yang telah terdaftar
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    val user = User(
                                            firebaseUser.uid,
                                            binding.registerEditTextFirstName.text.toString().trim { it <= ' ' },
                                            binding.registerEditTextLastName.text.toString().trim { it <= ' ' },
                                            binding.registerActivityEmail.text.toString().trim{it <= ' '},

                                    )
                                    FirestoreClass().registerUser(this, user)


                                    FirebaseAuth.getInstance().signOut()
                                    finish()

                                } else {
                                    hideProgressDialog()
                                    //jika register tidak berhasil maka pesan error akan muncul
                                    showErrorSnackBar(task.exception!!.message.toString(),true)
                                }
                            })
        }
    }

    fun userRegistrationSuccess(){
        //menyembunyikan progress dialog
        hideProgressDialog()

        Toast.makeText(this@RegisterActivity,
        resources.getString(R.string.register_success_msg),
        Toast.LENGTH_SHORT).show()



    }
}

