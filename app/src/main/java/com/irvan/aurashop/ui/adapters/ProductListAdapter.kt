package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.databinding.ItemListLayoutBinding
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.activities.ProductDetailsActivity
import com.irvan.aurashop.ui.fragments.ProductsFragment
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

open class
ProductListAdapter (

    private val context: Context,
    private var  list: ArrayList<Product>,
    private val fragment: ProductsFragment

) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.product_image, holder.itemView.findViewById(R.id.image_view_item_image))
            holder.itemView.findViewById<TextView>(R.id.item_list_product_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.item_list_product_price).text = "Rp.${model.product_price}"


            //ini adalah fungsi dimana tiap kali kita mengklik suatu produk akan diarahkan pada activity detail produk tersebut
            //serta mendapatkan id produk tersebut
            //EXTRA_PRODUCT_OWNER_ID adalah mendapatkan id user ketika user menambahkan produk tersebut ke dalam cart / keranjang,
            holder.itemView.setOnClickListener{
                val intent = Intent(context,ProductDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_PRODUCT_ID,model.product_id)
                intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}