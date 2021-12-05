package com.irvan.aurashop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast

import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityAddEditAddressBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Address
import com.irvan.aurashop.utils.Constant

class AddEditAddressActivity : BaseActivity() {

    private var mAddressDetails : Address? = null

    private lateinit var binding: ActivityAddEditAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        if (intent.hasExtra(Constant.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constant.EXTRA_ADDRESS_DETAILS)
        }

        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()){
                binding.addEditAddressSubmitBtn.text = resources.getString(R.string.update_label_btn)

                binding.addEditAddressInputFullname.setText(mAddressDetails?.name)
                binding.addEditAddressInputPhone.setText(mAddressDetails?.mobileNumber)
                binding.addEditAddressInputAddress.setText(mAddressDetails?.address)
                binding.addEditAddressInputZipcode.setText(mAddressDetails?.zipCode)
                binding.addEditAddressInputAdditional.setText(mAddressDetails?.additionalNote)

            }
        }

        binding.addEditAddressSubmitBtn.setOnClickListener{ saveAddressToFireStore() }


    }


    private fun setupActionBar(){

        setSupportActionBar(binding.addEditAddressToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.addEditAddressToolbar.setNavigationOnClickListener { onBackPressed() }

    }

    //Fungsi untuk memvalidasi data disaat pertama kalinya menginput alamat untuk pertama kalinya atau mengubah data alamat yang sudah ada
    private fun validateData():Boolean {
        return when {

            TextUtils.isEmpty(binding.addEditAddressInputFullname.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_please_enter_fullname),true
                )
                false
            }

            TextUtils.isEmpty(binding.addEditAddressInputPhone.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_please_enter_mobile),true
                )
                false
            }

            TextUtils.isEmpty(binding.addEditAddressInputAddress.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_input_addreess),true
                )
                false
            }

            TextUtils.isEmpty(binding.addEditAddressInputZipcode.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_input_zipcode),true
                )
                false
            }

            TextUtils.isEmpty(binding.addEditAddressInputAdditional.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_input_additional),true
                )
                false
            }

            else -> {
                true
            }
        }
    }

    //Menyimpan data alamat yang sudah di input pada class firestore
    private fun saveAddressToFireStore(){
        val fullName:String = binding.addEditAddressInputFullname.text.toString().trim { it <= ' ' }
        val phoneNumber:String = binding.addEditAddressInputPhone.text.toString().trim{it <= ' '}
        val address:String = binding.addEditAddressInputAddress.text.toString().trim { it <= ' ' }
        val zipCode:String = binding.addEditAddressInputZipcode.text.toString().trim { it <= ' '}
        val additionalNote:String = binding.addEditAddressInputAdditional.text.toString().trim { it <= ' ' }

            if (validateData()){
                showProgressDialog(resources.getString(R.string.please_wait_msg))

                val addressModel = Address(
                        FirestoreClass().getCurrentUserID(),
                        fullName,
                        phoneNumber,
                        address,
                        zipCode,
                        additionalNote,

                )
                if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()){
                    FirestoreClass().updateAddress(this, addressModel, mAddressDetails!!.id)
                } else {
                    FirestoreClass().addAddress(this@AddEditAddressActivity, addressModel)
                }
            }

    }

    //Memunculkan pesan apabbila proses penginputan berjalan dengan baik
    fun addUpdateAddressSuccess(){
        hideProgressDialog()

        if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()){
            showErrorSnackBar(resources.getString(R.string.err_your_address_updated_succesfully),false)
        } else {
            showErrorSnackBar(resources.getString(R.string.err_your_address_added_succesfully),false)
        }
        setResult(RESULT_OK)
        finish()
    }




 }