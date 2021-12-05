package com.irvan.aurashop.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()


        binding.forgetPasswordSubmitBtn.setOnClickListener {
            val email: String = binding.forgetPasswordEmail.text.toString().trim { it <= ' ' }
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.forget_password_email_empty),true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait_msg))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener{ task ->
                            hideProgressDialog()
                            if (task.isSuccessful){
                                Toast.makeText(this@ForgotPasswordActivity, resources.getString(R.string.sent_email_success_msg),
                                        Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                showErrorSnackBar(task.exception!!.message.toString(),true)
                            }
                        }
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(findViewById(R.id.forget_password_toolbar))

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.forgetPasswordToolbar.setNavigationOnClickListener{
            onBackPressed()
        }

    }
}