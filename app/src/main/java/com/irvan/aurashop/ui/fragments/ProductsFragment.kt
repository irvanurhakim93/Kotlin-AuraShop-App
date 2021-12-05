package com.irvan.aurashop.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.lifecycle.ViewModelProvider
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentProductsBinding
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.activities.AddProductActivity
import com.irvan.aurashop.ui.activities.SettingsActivity
import com.irvan.aurashop.ui.adapters.ProductListAdapter


class ProductsFragment : BaseFragment() {

    private var _binding:FragmentProductsBinding? = null
    private val binding get() = _binding!!

    //parameter tombol hapus & product fragment


//    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //jika kita ingin menggunakan option menu dalam fragment kita perlu menambahkannya seperti script di bawah
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater,container,false)

        return binding.root
    }



    fun deleteProduct(productID:String){
        showAlertDialogToDeleteProduct(productID)
    }

    private fun showAlertDialogToDeleteProduct(productID:String){
        val builder = AlertDialog.Builder(requireActivity())
        //memasang judul alert dialog disini
        builder.setTitle(resources.getString(R.string.product_delete_dialog_title))
        //memasang isi pesan alert dialog disini
        builder.setMessage(resources.getString(R.string.product_delete_dialog_message))

        //fungsi jika tombol "Ya" ditekan,disini disebut sebagai positive button
        builder.setPositiveButton(resources.getString(R.string.product_delete_no_btn)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        //fungsi jika tombol "Tidak" ditekan,disini disebut sebagai negative button
        //proses penghapusan sebenarnya telah dijabarkan pada FireStoreClass karena proses tersebut sekaligus menghapus data produk yang ada di FireStore
        builder.setNegativeButton(resources.getString(R.string.product_delete_yes_btn)){ dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait_msg))

            FirestoreClass().deleteProduct(this,productID)

            dialogInterface.dismiss()
        }

        //membuat alert dialog
        val alertDialog: AlertDialog = builder.create()
        //membuat properties dialog lain
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun productDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(requireActivity(),resources.getString(R.string.product_delete_success_msg),Toast.LENGTH_SHORT).show()

        //setelah proses penghapusan produk berhasil maka dilakukanlah fungsi dibawah untuk merefresh kembali daftar produk dari proses penghapusan tadi

        getProductListFromFirestore()
    }

    //mendapatkan data product dari firestore
    fun successProductListFromFirestore(productList: ArrayList<Product>){
        hideProgressDialog()

      if (productList.size > 0){
          binding.fragmentProductRecyclerview.visibility = View.VISIBLE
          binding.fragmentProductNoProductFoundMsg.visibility = View.GONE

          binding.fragmentProductRecyclerview.layoutManager = LinearLayoutManager(activity)
          binding.fragmentProductRecyclerview.setHasFixedSize(true)
          val adapterProduct = ProductListAdapter(requireActivity(), productList,this)
          binding.fragmentProductRecyclerview.adapter = adapterProduct
      } else {
          binding.fragmentProductRecyclerview.visibility = View.GONE
          binding.fragmentProductNoProductFoundMsg.visibility = View.VISIBLE
      }

    }

    //memanggil data dan menampilkan produk pada FireStoreClass
    private fun getProductListFromFirestore(){
        showProgressDialog(resources.getString(R.string.please_wait_msg))
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    //menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}