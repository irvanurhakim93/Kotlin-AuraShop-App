package com.irvan.aurashop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentAddToCartDialogBinding
import com.irvan.aurashop.firestore.FirestoreClass


class AddToCartDialog : DialogFragment() {

    private var _binding:FragmentAddToCartDialogBinding? =null
    private var productId:String=""
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.shape_border);
        _binding = FragmentAddToCartDialogBinding.inflate(inflater,container,false)

        return binding.root
    }

    private fun getProductDetails(){

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.widthPixels * 0.85).toInt()

    }



}