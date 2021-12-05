package com.irvan.aurashop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityCartListBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.CartItem
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.adapters.CartItemsListAdapter
import com.irvan.aurashop.utils.Constant

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartListBinding
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        binding.cartListCheckoutBtn.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constant.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        getProductList()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.cartListToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.cartListToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCartItemsList(cartList:ArrayList<CartItem>){
        hideProgressDialog()

        for (product in mProductsList){
            for (cartItem in cartList){
                if (product.product_id == cartItem.product_id){

                    cartItem.stock_quantity = product.product_quantity

                if (product.product_quantity.toInt() == 0){
                    cartItem.cart_quantity = product.product_quantity
                  }
                }
            }
        }

        mCartListItems = cartList


        if(mCartListItems.size > 0){ //jika nilai cart / keranjang tidak nol atau lebih dari kosong maka

            binding.cartListRecyclerview.visibility = View.VISIBLE //recyclerview atau daftar produk yang sebelumnya diperintahkan masuk keranjang akan muncul
            binding.cartListLinearleayoutCheckout.visibility = View.VISIBLE //linear layout khusus bagian checkout produk akan muncul
            binding.cartListNoFoundProductTxt.visibility = View.GONE //pesan "Keranjang Kosong" yang sebelumnya ada ketika keranjang sedang kosong maka akan menghilang

            binding.cartListRecyclerview.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.cartListRecyclerview.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity,mCartListItems, true)
            binding.cartListRecyclerview.adapter = cartListAdapter

            var subTotal: Double = 0.0 // double adalah fungsi untuk dapat membuat perjumlahan pada objek adapter cart

            for (item in mCartListItems){

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0){
                    val price = item.product_price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }//penjumlahan antara harga produk dan jumlah produk yang diambil pada cart
            }

            binding.cartListCheckoutSubtotal.text = "RP.$subTotal" //mendeklarasikan sub total
            binding.cartListCheckoutShippingCharge.text = "RP.20000" //mendeklarasikan ongkos kirim,ini bisa diganti sesuai keinginan atau sesuai aturan ongkos kirim yang telah ditentukan

            if (subTotal > 0){
                binding.cartListLinearleayoutCheckout.visibility = View.VISIBLE

                val total = subTotal + 20000 //subtotal ini ditambah dengan ongkir yang dideklarasikan tadi
                binding.cartListCheckoutMustPayTotal.text = "RP.$total"
            } else {
                binding.cartListLinearleayoutCheckout.visibility = View.GONE
            }

        } else { //jika nilai keranjang / cart tersebut kosong maka tampilan recycler view yang menampilkan item & linear layout untuk checkout menghilang
            binding.cartListRecyclerview.visibility = View.GONE
            binding.cartListLinearleayoutCheckout.visibility = View.GONE
            binding.cartListNoFoundProductTxt.visibility = View.VISIBLE //hanya pesan "keranjang kosong' ini yang akan muncul
        }

    }

    fun successProductsListFromFireStore(productsList:ArrayList<Product>){

        mProductsList = productsList

        getCartItemList()
    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getAllProductsList(this@CartListActivity)

    }

    private fun getCartItemList(){
        FirestoreClass().getCartList(this@CartListActivity)
    }

    fun itemUpdateSucces(){
        hideProgressDialog()
        getCartItemList()
    }

    fun itemRemovedSuccess(){
        hideProgressDialog()

        showErrorSnackBar(resources.getString(R.string.cart_deleted_successfully),true)

        getCartItemList()
    }




}