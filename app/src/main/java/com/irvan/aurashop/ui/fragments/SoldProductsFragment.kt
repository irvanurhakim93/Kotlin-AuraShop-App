package com.irvan.aurashop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentSoldProductsBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.SoldProducts
import com.irvan.aurashop.ui.adapters.SoldProductsListAdapter


class SoldProductsFragment : BaseFragment() {

    private var _binding: FragmentSoldProductsBinding? = null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))

        //memanggil fungsi getSoldProductsList dari FireStoreClass
        FirestoreClass().getSoldProductsList(this@SoldProductsFragment)
    }

    fun successSoldProductsList(soldproductsList: ArrayList<SoldProducts>){
        hideProgressDialog()
        if (soldproductsList.size > 0){
            binding.fragmentSoldProductItemsRecyclerview.visibility = View.VISIBLE
            binding.fragmentSoldProductNoSoldProductsFoundTxt.visibility = View.GONE

            binding.fragmentSoldProductItemsRecyclerview.layoutManager = LinearLayoutManager(activity)
            binding.fragmentSoldProductItemsRecyclerview.setHasFixedSize(true)

            val soldProductsListAdapter = SoldProductsListAdapter(requireActivity(), soldproductsList)
            binding.fragmentSoldProductItemsRecyclerview.adapter = soldProductsListAdapter

        }else{
            binding.fragmentSoldProductItemsRecyclerview.visibility = View.GONE
            binding.fragmentSoldProductNoSoldProductsFoundTxt.visibility = View.VISIBLE
        }
    }

    //menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}