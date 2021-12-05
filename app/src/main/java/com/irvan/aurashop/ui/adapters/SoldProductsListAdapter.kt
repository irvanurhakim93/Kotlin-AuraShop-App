package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.SoldProducts
import com.irvan.aurashop.utils.GlideLoader

open class SoldProductsListAdapter (
        private val context: Context,
        private var list: ArrayList<SoldProducts>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.product_image,holder.itemView.findViewById(R.id.image_view_item_image))

            holder.itemView.findViewById<TextView>(R.id.item_list_product_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.item_list_product_price).text = model.product_price


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)

}