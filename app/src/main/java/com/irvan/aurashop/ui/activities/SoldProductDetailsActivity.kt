package com.irvan.aurashop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivitySoldProductDetailsBinding
import com.irvan.aurashop.models.SoldProducts
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoldProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var productDetails: SoldProducts = SoldProducts()
        if (intent.hasExtra(Constant.EXTRA_SOLD_PRODUCTS_DETAILS)){
            productDetails = intent.getParcelableExtra<SoldProducts>(Constant.EXTRA_SOLD_PRODUCTS_DETAILS)!!

        }

        //membuat setup action bar untuk menampilkan tombol back di atas layar
        setupActionBar()
        //memanggil fungsi untuk mempopulasikan data pada UI
        setupUI(productDetails)

    }

    private fun setupActionBar() {

        setSupportActionBar(binding.soldProductDetailsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.soldProductDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI(productDetails:SoldProducts) {

        binding.soldProductDetailsIdOrder.text = productDetails.order_id

        //format penanggalan dimana tanggal transaksi akan ditampilkan dalam UI
        val dateFormat = "dd MMM yyyy HH:mm"
        //membuat DateFormatter object untuk menampilkan tanggal pada format pemesanan yang spesifik
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        //membuat object kalender yang akan mengkonversikan nilai tanggal dan waktu dalam millisecond atau millidetik
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        binding.soldProductDetailsDate.text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductDetailsActivity).loadProductPicture(
                productDetails.product_image,
                binding.soldProductDetailsItemImage
        )

        binding.soldProductDetailsItemName.text = productDetails.product_name
        binding.soldProductDetailsItemPrice.text = "${productDetails.product_price}"
        binding.soldProductQuantity.text = productDetails.sold_quantity
        binding.soldProductDetailsFullName.text = productDetails.address.name
        binding.soldProductDetailsAddress.text = "${productDetails.address.address}, ${productDetails.address.zipCode}"
        binding.soldProductDetailsAdditionalNote.text = productDetails.address.additionalNote

        if (productDetails.address.otherDetails.isNotEmpty()){
            binding.soldProductDetailsOtherDetails.visibility = View.VISIBLE
            binding.soldProductDetailsOtherDetails.text = productDetails.address.otherDetails
        } else {
            binding.soldProductDetailsOtherDetails.visibility = View.GONE
        }
        binding.soldProductDetailsMobileNumber.text = productDetails.address.mobileNumber
        binding.soldProductDetailsSubTotal.text = productDetails.sub_total_amount
        binding.soldProductDetailsShippingCharge.text = productDetails.shipping_charge
        binding.soldProductDetailsTotalAmount.text = productDetails.total_amount
    }
    //SELESAI

}