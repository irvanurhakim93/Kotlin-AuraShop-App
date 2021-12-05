package com.irvan.aurashop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityCheckOutBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Address
import com.irvan.aurashop.models.CartItem
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.adapters.CartItemsListAdapter
import com.irvan.aurashop.utils.Constant

class CheckOutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private lateinit var mOrderDetails: Orders
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0

    private lateinit var binding: ActivityCheckOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        if (intent.hasExtra(Constant.EXTRA_SELECTED_ADDRESS)){

            mAddressDetails = intent.getParcelableExtra<Address>(Constant.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails !== null){
            binding.checkoutFullName.text = mAddressDetails?.name
            binding.checkoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.checkoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()){
                binding.checkoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.checkoutMobileNumber.text = mAddressDetails?.mobileNumber
        }
        getProductList()

        binding.btnPlaceOrder.setOnClickListener { placeAnOrder() }

    }


    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }
        binding.toolbarCheckoutActivity.setOnClickListener { onBackPressed() }
    }

    //memuat data produk yang ada pada produk list pada class firestore
    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))

        FirestoreClass().getAllProductsList(this@CheckOutActivity)
    }

    //Mengumpulkan satu persatu data produk dari class model Product dan dihimpun pada getcartitemslist
    fun successProductsListFromFireStore(productList: ArrayList<Product>){
        mProductList = productList
        getCartItemsList()
    }

    //mendapatkan list atau daftar data keranjang dari firestore class
    private fun getCartItemsList(){
        FirestoreClass().getCartList(this@CheckOutActivity)
    }


    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()

        for (product in mProductList){
            for (cartItem in cartList){
                if (product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.product_quantity
                }
            }
        }
        mCartItemsList = cartList

        if (mCartItemsList.size > 0){
            binding.checkoutRecyclerviewItems.layoutManager = LinearLayoutManager(this@CheckOutActivity)
            binding.checkoutRecyclerviewItems.setHasFixedSize(true)

            //menggunakan adapter dari cart tetapi tombol seperti menambah atau mengurangi jumlah item produk tidak dapat digunakan
            val cartListAdapter = CartItemsListAdapter(this@CheckOutActivity, mCartItemsList, false)
            binding.checkoutRecyclerviewItems.adapter = cartListAdapter

            for (item in mCartItemsList){
                val availableQUantity = item.stock_quantity.toInt()
                if (availableQUantity > 0){
                    val price = item.product_price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    mSubTotal += (price * quantity)
                }
            }

            binding.checkoutSubTotalRupiah.text = "RP.$mSubTotal"
            binding.tvCheckoutShippingCharge.text = "RP 20000"

            if (mSubTotal > 0){
                binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

                mTotalAmount = mSubTotal + 20000
                binding.tvCheckoutTotalAmount.text = "RP.$mTotalAmount"
            } else {
                binding.llCheckoutPlaceOrder.visibility = View.GONE

            }

        } else {
            binding.checkoutRecyclerviewItems.visibility=View.GONE
            binding.llCheckoutPlaceOrder.visibility=View.GONE
            binding.llCheckoutAddressDetails.visibility=View.GONE
            binding.checkoutItemsReceipt.visibility=View.GONE
            binding.checkoutProductItems.visibility=View.GONE
            binding.checkoutSelectedAddress.visibility=View.GONE
        }

    }

    private fun placeAnOrder(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        mOrderDetails = Orders(
                FirestoreClass().getCurrentUserID(), //mendapatkan informasi id user menggunakan FireStore Class
                mCartItemsList,
                mAddressDetails!!,
                "${System.currentTimeMillis()}",
                mCartItemsList[0].product_image,
                mSubTotal.toString(),
                "20000", //contoh kasus dalam hal ini adalah ongkos kirimnya adalah RP 20 ribu
                mTotalAmount.toString(),
                System.currentTimeMillis()
        )
        FirestoreClass().placeOrder(this, mOrderDetails)

    }

    fun orderPlacedSuccess(){
        FirestoreClass().updateAllDetails(this, mCartItemsList, mOrderDetails)
    }


    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()

        hideProgressDialog()

        Toast.makeText(this,R.string.checkout_success_msg,Toast.LENGTH_SHORT).show()

        val intent = Intent(this@CheckOutActivity, PayAtPlaceActivity::class.java)
        startActivity(intent)
        finish()
    }

}