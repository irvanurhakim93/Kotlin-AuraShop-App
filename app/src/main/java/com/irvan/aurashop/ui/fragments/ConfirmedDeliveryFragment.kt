package com.irvan.aurashop.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.R.layout.fragment_confirmed_delivery
import com.irvan.aurashop.databinding.FragmentConfirmedDeliveryBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.activities.HomeActivity
import com.irvan.aurashop.ui.adapters.ConfirmedDeliveryAdapter


class ConfirmedDeliveryFragment : Fragment() {
    private var _binding:FragmentConfirmedDeliveryBinding? =null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentConfirmedDeliveryBinding.inflate(inflater, container, false)


        val payBtn:Button = binding.fragmentConfirmedDeliveryPayNowBtn
        val finishBtn:Button = binding.fragmentConfirmedDeliveryFinishbtn
        // Inflate the layout for this fragment
        payBtn.setOnClickListener {
            binding.fragmentConfirmedDeliveryRecyclerview.visibility = View.GONE
            binding.fragmentConfirmedDeliveryPayNowBtn.visibility = View.GONE
            binding.fragmentConfirmedDeliverySuccessPaymentIcon.visibility = View.VISIBLE
            binding.fragmentConfirmedDeliverySuccessMsgText.visibility = View.VISIBLE
            binding.fragmentConfirmedDeliveryFinishbtn.visibility = View.VISIBLE
        }

        finishBtn.setOnClickListener {
            val intent = Intent (this@ConfirmedDeliveryFragment.context, HomeActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }



    private fun getConfirmedOrder(){
        FirestoreClass().getConfirmedOrder(this@ConfirmedDeliveryFragment)
    }

    override fun onResume() {
        super.onResume()

        getConfirmedOrder()
    }

    fun successGetConfirmedOrder(pesanan:ArrayList<Orders>){
        if (pesanan.size > 0){
            binding.fragmentConfirmedDeliveryRecyclerview.visibility = View.VISIBLE
            binding.fragmentConfirmedDeliveryRecyclerview.layoutManager = LinearLayoutManager(activity)
            binding.fragmentConfirmedDeliveryRecyclerview.setHasFixedSize(true)

            val adapter = ConfirmedDeliveryAdapter(requireActivity(), pesanan)
            binding.fragmentConfirmedDeliveryRecyclerview.adapter = adapter
        } else {
            binding.fragmentConfirmedDeliveryRecyclerview.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }






}