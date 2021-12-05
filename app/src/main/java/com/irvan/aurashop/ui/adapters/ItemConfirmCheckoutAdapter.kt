package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.utils.GlideLoader

open class ItemConfirmCheckoutAdapter (
        private val context:Context,
        private var list:ArrayList<Orders>
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_confirm_checkout_layout,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(
                    model.product_image,
                    holder.itemView.findViewById(R.id.item_confirm_product_image)
            )

            holder.itemView.findViewById<TextView>(R.id.item_confirm_product_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.item_confirm_shipping_charge_rp).text = model.shipping_charge
            holder.itemView.findViewById<TextView>(R.id.item_confirm_total_payment_rp).text = "RP${model.total_amount}"
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}