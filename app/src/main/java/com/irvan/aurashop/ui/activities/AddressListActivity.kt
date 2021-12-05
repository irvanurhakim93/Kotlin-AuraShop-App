package com.irvan.aurashop.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityAddressListBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Address
import com.irvan.aurashop.ui.adapters.AddressListAdapter
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.SwipeToDeleteCallback
import com.irvan.aurashop.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding
    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        binding.addAddressListTxt.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent,Constant.ADD_ADDRESS_REQUEST_CODE)
        }
        getAddressList()
        if (intent.hasExtra(Constant.EXTRA_SELECT_ADDRESS)){
            mSelectAddress = intent.getBooleanExtra(Constant.EXTRA_SELECT_ADDRESS, false)
        }

        if (mSelectAddress){
            binding.addressListToolbarTitle.text = resources.getString(R.string.title_select_address)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            getAddressList()
        }
    }

    private fun getAddressList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getAddressesList(this)
    }

    fun successAddressListFromFireStore(addressList: ArrayList<Address>){
        hideProgressDialog()
        if(addressList.size > 0){
            //jika nilai address list tidak kosong maka recyclerview list alamat akan muncul
            //dan tulisan "tidak ada alamat" menghilang
            binding.addressListRecyclerview.visibility = View.VISIBLE
            binding.addressListNotFound.visibility = View.GONE

            //menampilkan list - list atau daftar alamat secara terperinci
            binding.addressListRecyclerview.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.addressListRecyclerview.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList, mSelectAddress)
            binding.addressListRecyclerview.adapter = addressAdapter

            if (!mSelectAddress){
                //menjalankan fungsi swipe untuk mengedit objek alamat adapter
                val editSwipeHandler = object : SwipeToEditCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = binding.addressListRecyclerview.adapter as AddressListAdapter
                        adapter.notifyEditItem(
                                this@AddressListActivity,
                                viewHolder.adapterPosition
                        )
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.addressListRecyclerview)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog(resources.getString(R.string.please_wait_msg))

                        FirestoreClass().deleteAddress(this@AddressListActivity, addressList[viewHolder.adapterPosition].id)
                    }

                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.addressListRecyclerview)
            }




        } else{
            //sebaliknya,jika nilai address list kosong maka recycler view list alamat tidak akan muncul
            //dan tulisan "tidak ada alamat" akan muncul
            binding.addressListRecyclerview.visibility = View.GONE
            binding.addressListNotFound.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess(){
        hideProgressDialog()

        showErrorSnackBar(resources.getString(R.string.err_your_address_deleted_succesfully),true)

        getAddressList()
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.addressListToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.addressListToolbar.setNavigationOnClickListener { onBackPressed() }

    }
}