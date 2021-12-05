package com.irvan.aurashop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityProductDetailsBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.CartItem
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.models.WishlistItem
import com.irvan.aurashop.ui.fragments.AddToCartDialog
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    //binding untuk pengganti kotlin syntethic
    private lateinit var  mProductDetails:Product
    private lateinit var binding: ActivityProductDetailsBinding
    //variabel global untuk id produk
    private var mProductId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //fungsi mendapatkan id produk dalam activity ini
        if (intent.hasExtra(Constant.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constant.EXTRA_PRODUCT_ID)!!
        }
        var mProductOwnerId : String =""

        if (intent.hasExtra(Constant.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwnerId = intent.getStringExtra(Constant.EXTRA_PRODUCT_OWNER_ID)!!
        }

        setupActionBar()

        if (FirestoreClass().getCurrentUserID() == mProductOwnerId){
            binding.productDetailsAddToCartBtn.visibility = View.GONE
            binding.productDetailsGoToCartBtn.visibility = View.GONE
        } else {
            binding.productDetailsAddToCartBtn.visibility = View.VISIBLE
        }


        binding.addToWishlistBtn.setOnClickListener(this)
        binding.productDetailsGoToCartBtn.setOnClickListener(this)
        binding.productDetailsAddToCartBtn.setOnClickListener(this)

        getProductDetails()

    }



    //mendapatkan data detail produk dari firestore dan memunculkannya pada activity ini
    private fun getProductDetails(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getProductDetails(this,mProductId)
    }

    //memuat detail produk dimulai dari gambar dengan GlideLoader
    //lalu memuat nama produk,harga dan deskripsi dengan data class binding memanggil id xml yang sebelumnya telah dideklarasikan
    //mProductDetails adalah parameter global untuk mengambil data produk jika terjadinya penmbahan data produk ke dalam cart / keranjang
    fun productDetailSuccess(product: Product){
        mProductDetails = product
//        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(product.product_image,binding.productDetailsImageview)
        binding.productDetailsProductName.text = product.product_name
        binding.productDetailsProductPrice.text = "Rp.${product.product_price}"
        binding.productDetailsDescription.text = product.product_description
        binding.productDetailsAvailableQuantiy.text = product.product_quantity

        if (product.product_quantity.toInt() == 0){
            hideProgressDialog()

            binding.productDetailsAddToCartBtn.visibility = View.GONE
            binding.productDetailsAvailableQuantiy.text = resources.getString(R.string.lbl_out_of_stock)

            binding.productDetailsAvailableQuantiy.setTextColor(ContextCompat.getColor(this@ProductDetailsActivity, R.color.snackBarError))
        } else {
            if (FirestoreClass().getCurrentUserID() == product.user_id){
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistedInCart(this, mProductId)
            }
        }
    }

    fun productExistedInCart(){
        hideProgressDialog()
        binding.productDetailsAddToCartBtn.visibility = View.GONE
        binding.productDetailsGoToCartBtn.visibility = View.VISIBLE
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.productDetailsToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.productDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToWishlist(){
        val addToWishlist = WishlistItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.product_name,
            mProductDetails.product_price,
            mProductDetails.product_image,
        )

        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().addWishlistItems(this,addToWishlist)
    }

    //fungsi untuk mempersiapkan item keranjang untuk ditambahkan ke collection cart_items di firestore
    private fun addToCart(){


        val addToCart = CartItem(
                FirestoreClass().getCurrentUserID(),
                mProductId,
                mProductDetails.product_name,
                mProductDetails.product_price,
                mProductDetails.product_image,
                Constant.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().addCartItems(this, addToCart)
    }

    //menampilkan pesan apabila proses penambahan produk ke keranjang berhasil
    fun addToCartSuccess(){
        hideProgressDialog()

        showErrorSnackBar(resources.getString(R.string.success_add_to_cart_msg),false)

        binding.productDetailsAddToCartBtn.visibility = View.GONE
        binding.productDetailsGoToCartBtn.visibility = View.VISIBLE
    }

    fun addToWishlistSuccess(){
        hideProgressDialog()
        showErrorSnackBar(resources.getString(R.string.success_add_to_wishlist_msg),false)
    }


    //jika tombol add to cart tersebut diklik,maka fungsi tersebut di panggil guna untuk mengeksekusi
    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.product_details_add_to_cart_btn -> {
                    addToCart()
                }

                R.id.add_to_wishlist_btn -> {
                    addToWishlist()
                }

                R.id.product_details_go_to_cart_btn -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }

        }
    }


}