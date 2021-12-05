package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Orders

open class ConfirmedDeliveryAdapter (
        private val context:Context,
        private val list:ArrayList<Orders>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_confirmed_delivery,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.findViewById<TextView>(R.id.item_confirmed_delivery_total_price_rp).text=model.sub_total_amount
        holder.itemView.findViewById<TextView>(R.id.item_confirmed_delivery_shipping_charge_rp).text=model.shipping_charge
        holder.itemView.findViewById<TextView>(R.id.item_confirmed_delivery_detail_total_shop_rp).text=model.total_amount
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view)

}