package com.irvan.aurashop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.FragmentDashboardBinding

import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.activities.CartListActivity
import com.irvan.aurashop.ui.activities.ProductDetailsActivity
import com.irvan.aurashop.ui.activities.SettingsActivity
import com.irvan.aurashop.ui.activities.WishlistActivity
import com.irvan.aurashop.ui.adapters.DashboardItemListAdapter
import com.irvan.aurashop.utils.Constant

class HomeFragment : BaseFragment() {

    private var _binding:FragmentDashboardBinding? = null
    private val binding get() = _binding!!

//    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //jika kita ingin menggunakan option menu dalam fragment kita perlu menambahkannya seperti script di bawah
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemList()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =  ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_wishlist -> {
                startActivity(Intent(activity, WishlistActivity::class.java))
                return true
            }

            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }

            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun successDashBoardItemList(dashboardItemList: ArrayList<Product>){
        hideProgressDialog()
        if (dashboardItemList.size > 0){
            binding.fragmentDashboardRecyclerview.visibility = View.VISIBLE
            binding.fragmentDashboardTextNoFound.visibility = View.GONE

            binding.fragmentDashboardRecyclerview.layoutManager = GridLayoutManager(activity,2)
            binding.fragmentDashboardRecyclerview.setHasFixedSize(true)

            val adapter = DashboardItemListAdapter(requireActivity(),dashboardItemList)
            binding.fragmentDashboardRecyclerview.adapter = adapter

            //mengarahkan kepada detail produk ketika mengklik salah satu kotak produk di fragment dashboard
            //serta menampilkan details data dari sebuah produk menggunakan id khusus dalam class constant
            adapter.setOnClickListener(object: DashboardItemListAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constant.EXTRA_PRODUCT_ID, product.product_id)
                    intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                    startActivity(intent)
                }
            })

        }else{
            binding.fragmentDashboardRecyclerview.visibility = View.GONE
            binding.fragmentDashboardTextNoFound.visibility = View.VISIBLE

        }
    }

    private fun getDashboardItemList(){
        //menunjukkan progress dialog
        showProgressDialog(resources.getString(R.string.please_wait_msg))

        FirestoreClass().getDashboardItemList(this@HomeFragment)
    }

    //untuk menghindari kebocoran memori
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}