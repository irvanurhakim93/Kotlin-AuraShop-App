package com.irvan.aurashop.ui.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityBillsAfterCheckoutBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.utils.Constant
import java.io.File
import java.io.IOException

class BillsAfterCheckoutActivity : BaseActivity(), View.OnClickListener {
    private var billsPhotoUri: Uri? = null
    private lateinit var binding: ActivityBillsAfterCheckoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillsAfterCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var infoBills: Orders = Orders()
        if (intent.hasExtra(Constant.EXTRA_MY_ORDER_DETAILS)) {
            infoBills = intent.getParcelableExtra<Orders>(Constant.EXTRA_MY_ORDER_DETAILS)!!
        }

        BillsUI(infoBills)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.billsTakeCameraBtn.setOnClickListener(this@BillsAfterCheckoutActivity)
        binding.billsConfirmButton.setOnClickListener(this@BillsAfterCheckoutActivity)
    }



    private fun BillsUI(billsDetails: Orders) {
        binding.billsReceiverName.text = billsDetails.address.name
    }


    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.bills_take_camera_btn -> {
                takePhoto()
                }
                R.id.bills_confirm_button -> {
                    if (billsPhotoUri !=null){
                        FirestoreClass().uploadImageToCloudStorage(this,billsPhotoUri,Constant.CONFIRMED_PRODUCT)
                    } else {
                        completingPhoto()
                    }
                }
            }
        }
    }

    private fun completingPhoto() {

        Toast.makeText(this@BillsAfterCheckoutActivity,resources.getString(R.string.confirmed_product_success_msg),Toast.LENGTH_LONG).show()

        startActivity(Intent(this@BillsAfterCheckoutActivity, HomeActivity::class.java))
    }

    fun takePhoto(){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filePhoto = getPhotoFile(FILE_NAME)
        val providerFile =FileProvider.getUriForFile(this,"com.irvan.aurashop", filePhoto)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        if (takePhotoIntent.resolveActivity(this.packageManager) != null){
            startActivityForResult(takePhotoIntent, REQUEST_CODE)
        }else {
            Toast.makeText(this,"Tidak dapat membuka kamera", Toast.LENGTH_SHORT).show()
        }

    }



    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?){
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
            binding.billsImageview.setImageBitmap(takenPhoto)
        } else {
            super.onActivityResult(requestCode,resultCode,data)
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            binding.billsImageview.setImageURI(data?.data)
        }
    }
}

private lateinit var filePhoto: File
private const val REQUEST_CODE = 13
private const val FILE_NAME = "photo.jpg"

