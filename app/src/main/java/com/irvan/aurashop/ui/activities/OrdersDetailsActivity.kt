package com.irvan.aurashop.ui.activities


import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityOrderDetailsBinding
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.adapters.CartItemsListAdapter
import com.irvan.aurashop.utils.Constant
import java.util.*
import java.util.concurrent.TimeUnit

class OrdersDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        var MyOrderDetails: Orders = Orders()
        if (intent.hasExtra(Constant.EXTRA_MY_ORDER_DETAILS)){
            MyOrderDetails = intent.getParcelableExtra<Orders>(Constant.EXTRA_MY_ORDER_DETAILS)!!


        }
        setupUI(MyOrderDetails)

    }


    private fun setupActionBar(){

        setSupportActionBar(binding.activityOrdersToolbar)

        val actionBar = supportActionBar
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_32dp)
        }

        binding.activityOrdersToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupUI(orderDetails: Orders){
        binding.orderDetailsIdTxt.text = orderDetails.product_name

        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDatetime = formatter.format(calendar.time)
        binding.orderDetailsDateTxt.text = orderDatetime

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Perbedaan status pengiriman dalam jam", "$diffInHours")

        when{
            diffInMilliSeconds < 60000 -> { //jika status kurang dari satu jam maka pengiriman barang sedang pending
                binding.orderStatusTxt.text = resources.getString(R.string.order_status_pending)
                binding.orderStatusTxt.setTextColor(ContextCompat.getColor(this@OrdersDetailsActivity, R.color.red))
            }
            diffInMilliSeconds < 180000 -> { //jika status kurang dari dua jam maka pengiriman barang sedang pending
                binding.orderStatusTxt.text = resources.getString(R.string.order_status_in_process)
                binding.orderStatusTxt.setTextColor(ContextCompat.getColor(this@OrdersDetailsActivity, R.color.colorOrderStatusInProcess))
            }
            else -> { //else adalah pengecualian dari kedua kode perbedaan status pengiriman dalam jam di atas,jika contoh kasusnya pengiriman tepat waktu
                binding.orderStatusTxt.text = resources.getString(R.string.order_status_delivered)
                binding.orderStatusTxt.setTextColor(ContextCompat.getColor(this@OrdersDetailsActivity, R.color.colorOrderStatusDelivered))
            }
        }

        binding.orderItemsRecyclerview.layoutManager = LinearLayoutManager(this@OrdersDetailsActivity)
        binding.orderItemsRecyclerview.setHasFixedSize(true)

        var cartListAdapter = CartItemsListAdapter(this@OrdersDetailsActivity, orderDetails.items, false)
        binding.orderItemsRecyclerview.adapter = cartListAdapter

        binding.orderDetailsFullName.text = orderDetails.address.name
        binding.orderDetailsAddress.text = "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        binding.orderDetailsAdditionalNote.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()){
            binding.orderDetailsOtherDetails.visibility = View.VISIBLE
            binding.orderDetailsOtherDetails.text = orderDetails.address.otherDetails
        } else {
            binding.orderDetailsOtherDetails.visibility = View.GONE
        }
        binding.orderDetailsMobileNumber.text = orderDetails.address.mobileNumber

        binding.orderDetailsSubTotal.text = orderDetails.sub_total_amount
        binding.orderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.orderDetailsTotalAmount.text = orderDetails.total_amount

    }

}