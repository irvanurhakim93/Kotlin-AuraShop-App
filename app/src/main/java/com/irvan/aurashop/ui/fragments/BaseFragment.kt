package com.irvan.aurashop.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentBaseBinding


open class BaseFragment : Fragment(R.layout.fragment_base) {

    private var _binding: FragmentBaseBinding? = null
    private val  binding get() = _binding!!

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(requireActivity())

        //Mengatur konten/isi layar dari sumber layout
        //
        mProgressDialog.setContentView(R.layout.dialog_progress_bar)

        mProgressDialog.findViewById<TextView>(R.id.progressTitle).text = text

        mProgressDialog.setCancelable(false)

        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog(){

        mProgressDialog.dismiss()
    }

    //menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}
