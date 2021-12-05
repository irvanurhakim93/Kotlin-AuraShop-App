package com.irvan.aurashop.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityWishlistBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.models.WishlistItem
import com.irvan.aurashop.ui.adapters.WishListItemsListAdapter

class WishlistActivity : BaseActivity() {

    private lateinit var binding:ActivityWishlistBinding
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mWishlistItems:ArrayList<WishlistItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
    }

    override fun onResume() {
        super.onResume()

        getProductList()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.wishListToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.wishListToolbar.setNavigationOnClickListener { onBackPressed() }
    }




    fun SuccessWishlistItem(wishList:ArrayList<WishlistItem>){
        hideProgressDialog()

        for (product in mProductsList){
            for (wishItem in wishList){
                if (product.product_id == wishItem.product_id){

                    wishItem.stock_quantity = product.product_quantity

                }
            }
        }

        mWishlistItems = wishList

        if (mWishlistItems.size > 0){
            binding.wishListRecyclerview.visibility = View.VISIBLE
            binding.wishListNoFoundProductTxt.visibility = View.GONE

            binding.wishListRecyclerview.layoutManager = LinearLayoutManager(this@WishlistActivity)
            binding.wishListRecyclerview.setHasFixedSize(true)

            val wishListAdapter = WishListItemsListAdapter(this@WishlistActivity,mWishlistItems,true)
            binding.wishListRecyclerview.adapter = wishListAdapter
        } else {
            binding.wishListRecyclerview.visibility = View.GONE
            binding.wishListNoFoundProductTxt.visibility = View.VISIBLE

        }

    }

    fun successProductsListFromFirestore(productList:ArrayList<Product>){
        mProductsList = productList

        getWishlistItemList()
    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getAllProductsList(this@WishlistActivity)

    }

    private fun getWishlistItemList(){
        FirestoreClass().getWishlistItem(this@WishlistActivity)
    }

    fun itemUpdateSuccess(){
        hideProgressDialog()
        getWishlistItemList()
    }


    fun itemRemovedSuccess(){
        hideProgressDialog()

        showErrorSnackBar(resources.getString(R.string.wishlist_deleted_successfully),true)

        getWishlistItemList()
    }
}