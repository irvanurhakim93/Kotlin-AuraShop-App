package com.irvan.aurashop.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityAddProductBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader
import java.io.IOException



class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileURI: Uri? = null
    private var mProductImageURL: String =""

    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        binding.addProductImage.setOnClickListener(this)

        binding.addProductSubmitBtn.setOnClickListener(this)
    }


    private fun setupActionBar(){

        setSupportActionBar(binding.addProductToolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.addProductToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null ){
            when(v.id){
                R.id.add_product_image -> {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constant.showImageChooser(this@AddProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constant.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.add_product_submit_btn -> {
                    if (validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileURI, Constant.PRODUCT_IMAGE)
    }

    fun productUploadSuccessPopup(){
        hideProgressDialog()
        Toast.makeText(this@AddProductActivity,resources.getString(R.string.product_uploaded_success_message),Toast.LENGTH_SHORT).show()
        finish()
    }

    fun imageUploadSuccess(imageURL: String){
//        hideProgressDialog()

//        showErrorSnackBar("Proses mengunggah foto produk anda telah berhasil. URL foto produk anda adalah: $imageURL,",false)

        mProductImageURL = imageURL

        uploadProductDetails()


    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(Constant.APP_PREFERENCE_NAME, Context.MODE_PRIVATE).getString(Constant.LOGGED_IN_USERNAME, "")!!

        val product = Product(
                FirestoreClass().getCurrentUserID(),
                username,
                binding.addProductEditTextTitleProduct.text.toString().trim {it <= ' '},
                binding.addProductEditTextProductPrice.text.toString().trim {it <= ' '},
                binding.addProductEditTextProductDesc.text.toString().trim {it <= ' '},
                binding.addProductEditTextProductQuantity.text.toString().trim {it <= ' '},
                mProductImageURL
        )

        FirestoreClass().uploadProductDetails(this,product)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.READ_STORAGE_PERMISSION_CODE){
            //jika permintaan perizinan mengakses internal memori dikabulkan
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constant.showImageChooser(this)
            } else {
                //Menampilkan pesan toast jika perizinan mengakses internal memori ditolak
                Toast.makeText(this,resources.getString(R.string.read_storage_permission_denied_msg), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constant.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    binding.updateProductImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_edit_24))

                    mSelectedImageFileURI = data.data!!

                    try {
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!, binding.addProductImage
                        )
                    } catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Permintaan dibatalkan","Pemilihan foto dibatalkan")
        }
    }

    private fun validateProductDetails(): Boolean {
        return when{

            mSelectedImageFileURI == null -> {
                showErrorSnackBar(resources.getString(R.string.error_select_product_image),true)
                false
            }

            TextUtils.isEmpty(binding.addProductEditTextTitleProduct.text.toString().trim { it <=' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_product_name),true)
                false
            }

            TextUtils.isEmpty(binding.addProductEditTextProductPrice.text.toString().trim { it <=' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_product_price),true)
                false
            }

            TextUtils.isEmpty(binding.addProductEditTextProductDesc.text.toString().trim { it <=' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_product_description),true)
                false
            }

            TextUtils.isEmpty(binding.addProductEditTextProductQuantity.text.toString().trim { it <=' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_input_product_quantity),true)
                false
            }
            else -> {
                true
            }
        }

        }
}

