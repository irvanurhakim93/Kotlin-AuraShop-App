package com.irvan.aurashop.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityCompleteProfileUserBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.User
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader
import java.io.IOException


class CompleteProfileUserActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCompleteProfileUserBinding

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteProfileUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS)){
            //Mendapatkan detail user dari intent sebagai data ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)!!
        }

        binding.completeProfileEditTextFirstName.setText(mUserDetails.firstName)
        binding.completeProfileEditTextLastName.setText(mUserDetails.lastName)
        binding.completeProfileEditTextEmail.isEnabled = false
        binding.completeProfileEditTextEmail.setText(mUserDetails.email)

        if (mUserDetails.profileCompleted ==0){
            binding.profileTitleText.text = resources.getString(R.string.complete_profile_label)
            binding.completeProfileEditTextFirstName.isEnabled = false

            binding.completeProfileEditTextLastName.isEnabled = false
        } else {
            setupActionBar()
            binding.profileTitleText.text = resources.getString(R.string.settings_profile_label)
            GlideLoader(this@CompleteProfileUserActivity).loadUserPicture(mUserDetails.image, binding.completeProfileImage)

            binding.completeProfileEditTextEmail.isEnabled = false
            binding.completeProfileEditTextEmail.setText(mUserDetails.email)

            if(mUserDetails.mobile != 0L ){
                binding.completeProfileEditTextMobileNumber.setText(mUserDetails.mobile.toString())
            }

            if (mUserDetails.gender == Constant.PRIA){
                binding.completeProfileRadioButtonMale.isChecked = true
            } else {
                binding.completeProfileRadioButtonFemale.isChecked = true
            }
        }

        binding.completeProfileImage.setOnClickListener(this@CompleteProfileUserActivity)
        binding.completeProfileSubmitBtn.setOnClickListener(this@CompleteProfileUserActivity)

    }

    private fun setupActionBar(){

         setSupportActionBar(binding.completeProfileToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.completeProfileToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.complete_profile_image -> {
                    //Disini kita akan memeriksa apakah permission atau perizinan sudah diperbolehkan atau harus memmbuat request atau permintaan terlebih dahulu
                    //Pertama - tama kita akan memeriksa READ_INTERNAL_STORAGE apakah sudah diterapkan dalam android manifest
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        showErrorSnackBar("Anda telah mengizinkan permintaan",false)
                        Constant.showImageChooser(this)
                    } else {
                        //untuk mendapatkan perizinan dari request yang dibuat,maka permission tersebut harus dicantumkan dalam manifest
                        ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constant.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.complete_profile_submit_btn -> {


                    if (validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait_msg))

                        if (mSelectedImageFileUri != null)
                        FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri, Constant.USER_PROFILE_IMAGE)
                        else {
                            completeUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun completeUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.completeProfileEditTextFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName){
            userHashMap[Constant.FIRST_NAME] = firstName
        }

        val lastName = binding.completeProfileEditTextLastName.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName){
            userHashMap[Constant.LAST_NAME] = lastName
        }

        val mobileNumber = binding.completeProfileEditTextMobileNumber.text.toString().trim{it <= ' '}

        val gender = if (binding.completeProfileRadioButtonMale.isChecked){
            Constant.PRIA
        } else {
            Constant.WANITA
        }

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constant.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
            userHashMap[Constant.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constant.GENDER] = gender
        }

        // Key: gender value: Pria
        //gender:Pria
        userHashMap[Constant.GENDER] = gender

        userHashMap[Constant.COMPLETED_PROFILE] = 1

//        showProgressDialog(resources.getString(R.string.please_wait_msg))

        FirestoreClass().completeUserProfileData(this, userHashMap)
    }

    fun completeProfileSuccess(){
        hideProgressDialog()

        Toast.makeText(this@CompleteProfileUserActivity,resources.getString(R.string.complete_profile_success_msg),Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@CompleteProfileUserActivity, HomeActivity::class.java))
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //jika permission dikabulkan
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
//            showErrorSnackBar("Permintaan membuka galeri telah diizinkan",false)
            Constant.showImageChooser(this)
        } else {
            //menampilkan toast jika permission tidak dikabulkan
            Toast.makeText(this,resources.getString(R.string.read_storage_permission_denied_msg),Toast.LENGTH_LONG).show()
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constant.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                try{
                    mSelectedImageFileUri = data.data!!
                    GlideLoader(this@CompleteProfileUserActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.completeProfileImage
                    )
                } catch (e:IOException){
                    e.printStackTrace()
                    Toast.makeText(
                            this@CompleteProfileUserActivity,
                            resources.getString(R.string.error_select_product_image),
                            Toast.LENGTH_LONG
                    ).show()
                }
                }
            }

        }
    }
    private fun validateUserProfileDetails(): Boolean{
        return when{
            TextUtils.isEmpty(binding.completeProfileEditTextMobileNumber.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_phone),true)
                false
            } else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String){
//        hideProgressDialog()

        mUserProfileImageURL = imageURL
        completeUserProfileDetails()
    }
}
