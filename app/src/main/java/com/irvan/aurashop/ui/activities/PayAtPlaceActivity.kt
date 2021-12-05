package com.irvan.aurashop.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ActivityPayAtPlaceBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.adapters.PayAtPlaceTabAdapter
import com.irvan.aurashop.utils.Constant

class PayAtPlaceActivity : BaseActivity(), View.OnClickListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var binding:ActivityPayAtPlaceBinding
    private lateinit var checkoutList: ArrayList<Orders>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayAtPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tabLayout = findViewById(R.id.pay_at_place_tabLayout)
        viewPager = findViewById(R.id.pay_at_place_viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Estimasi Pengiriman"))
        tabLayout.addTab(tabLayout.newTab().setText("Konfirmasi Pembayaran"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = PayAtPlaceTabAdapter(this, supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
        }
        )


        if (intent.hasExtra(Constant.SOLD_PRODUCTS)){

        }

        getOrderData()
    }

    private fun getOrderData(){
        FirestoreClass().getCheckoutList(this@PayAtPlaceActivity)
    }

    fun successGetOrderData(orderedProducts:ArrayList<Orders>){
        checkoutList = orderedProducts

    }


    override fun onResume() {
        super.onResume()

        getOrderData()
    }


    override fun onClick(v: View?) {
//
    }


}