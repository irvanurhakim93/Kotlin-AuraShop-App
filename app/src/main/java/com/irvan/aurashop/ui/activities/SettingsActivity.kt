package com.irvan.aurashop.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivitySettingsBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.User
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
        binding.settingsActivityEditBtn.setOnClickListener(this)
        binding.settingsLogoutBtn.setOnClickListener(this)
        binding.settingsLinearlayoutAddresses.setOnClickListener(this)
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.settingsActivityToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.settingsActivityToolbar.setNavigationOnClickListener { onBackPressed() }

    }

    //memanggil data detail user pada firestoreclass
    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getUserDetails(this)
    }

    //memanggil data detail user pada User dalam package Model
    fun userDetailSuccess(user: User){
        mUserDetails = user

        hideProgressDialog()

        //Fungsi loaduserpicture di class glideloader membutuhkan tipe data string sehingga tipe data image
        // berbentuk uri dirubah ke any,yang artinya tipe data tersebut dapat dikonversi ke tipe data manapun

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.settingsUserPhoto)
        binding.settingsUserName.text ="${user.firstName} ${user.lastName}"
        binding.settingsGender.text = user.gender
        binding.settingsEmail.text = user.email
        binding.settingsMobileNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    //menjalankan fungsi tombol logout pada activity dengan mengakhiri sesi FirebaseAuth
    //intent.flags dibutuhkan karena ketika user logout tidak akan menetap pada tampilan SettingsActivitiy melainkan akan diarahkan
    //pada class lain lain yakni LoginActivity

    override fun onClick(v: View?) {
        if (v != null ){
            when(v.id){

                R.id.settings_activity_edit_btn -> {
                    val intent = Intent(this@SettingsActivity, CompleteProfileUserActivity::class.java)
                    intent.putExtra(Constant.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }



                R.id.settings_logout_btn -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.settings_linearlayout_addresses -> {
                    val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}