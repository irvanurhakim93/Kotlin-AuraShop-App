package com.irvan.aurashop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentOrdersBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.adapters.OrdersListAdapter


class OrdersDetailsFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

//    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)


        return binding.root
    }


    private fun getMyOrdersList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))

        FirestoreClass().getMyOrdersList(this@OrdersDetailsFragment)
    }

    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }

    //untuk menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun populateOrdersListInUI(ordersList: ArrayList<Orders>){
        hideProgressDialog()

        if(ordersList.size > 0){
            binding.fragmentOrdersRecyclerview.visibility = View.VISIBLE
            binding.fragmentOrdersNoFoundTxt.visibility = View.GONE

            binding.fragmentOrdersRecyclerview.layoutManager = LinearLayoutManager(activity)
            binding.fragmentOrdersRecyclerview.setHasFixedSize(true)

            val ordersListAdapter = OrdersListAdapter(requireActivity(), ordersList)
            binding.fragmentOrdersRecyclerview.adapter = ordersListAdapter
        } else {
            binding.fragmentOrdersRecyclerview.visibility = View.GONE
            binding.fragmentOrdersNoFoundTxt.visibility = View.VISIBLE
        }

    }

}