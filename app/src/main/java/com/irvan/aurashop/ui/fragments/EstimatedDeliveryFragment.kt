package com.irvan.aurashop.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentEstimatedDeliveryBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Address
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.adapters.EstimatedAddressAdapter


class EstimatedDeliveryFragment : BaseFragment() {
    private var _binding:FragmentEstimatedDeliveryBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEstimatedDeliveryBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun estimatedDeliveryStatus(){
        val diffInMilliSeconds:Long = System.currentTimeMillis()

        when{
            diffInMilliSeconds < 60000 -> {
//                view?.findViewById<TextView>(R.id.fragment_estimated_delivery_shipping_status)?.text =  resources.getString(R.string.order_status_pending)

            }

            diffInMilliSeconds < 180000 -> { //jika status kurang dari dua jam maka pengiriman barang sedang pending
//                view?.findViewById<TextView>(R.id.fragment_estimated_delivery_shipping_status)?.text = resources.getString(R.string.order_status_in_process)
//                view?.findViewById<TextView>(R.id.fragment_estimated_delivery_shipping_status).setTextColor(context?.let {
//                    ContextCompat.getColor(
//                        it, R.color.colorOrderStatusInProcess)
//                })
            }

            else -> {
//                binding.fragmentEstimatedDeliveryShippingStatus.text = resources.getString(R.string.order_status_delivered)
//                binding.fragmentEstimatedDeliveryShippingStatus.setTextColor(ContextCompat.getColor(this@OrdersDetailsActivity, R.color.colorOrderStatusDelivered))
            }

        }
    }

    private fun getEstimatedDataFromFirestore(){
        FirestoreClass().getAddressEstimatedDelivery(this@EstimatedDeliveryFragment)
    }

    override fun onResume() {
        super.onResume()

        getEstimatedDataFromFirestore()
    }

    fun successGetEstimatedDeliveryData(addresses:ArrayList<Address>){
        if (addresses.size > 0){
            binding.fragmentEstimatedRecyclerview.visibility = View.VISIBLE
            binding.fragmentEstimatedRecyclerview.layoutManager = LinearLayoutManager(activity)
            binding.fragmentEstimatedRecyclerview.setHasFixedSize(true)

            val adapter = EstimatedAddressAdapter(requireActivity(), addresses)
            binding.fragmentEstimatedRecyclerview.adapter = adapter
        } else {
            binding.fragmentEstimatedRecyclerview.visibility=View.GONE
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}