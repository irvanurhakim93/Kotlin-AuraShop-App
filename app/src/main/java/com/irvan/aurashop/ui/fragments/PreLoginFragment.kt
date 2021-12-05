package com.irvan.aurashop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentPreLoginBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.activities.ProductDetailsActivity
import com.irvan.aurashop.ui.adapters.DashboardItemListAdapter
import com.irvan.aurashop.utils.Constant


class PreLoginFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var _binding:FragmentPreLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreLoginBinding.inflate(inflater,container,false)

        return binding.root

    }






    //untuk menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}