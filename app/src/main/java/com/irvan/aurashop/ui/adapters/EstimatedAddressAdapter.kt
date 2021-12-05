package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Address

open class EstimatedAddressAdapter (
        private val context:Context,
        private val list: ArrayList<Address>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_estimated_delivery,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.findViewById<TextView>(R.id.item_estimated_name_receipt_address).text = model.name
        holder.itemView.findViewById<TextView>(R.id.item_estimated_receipt_address_detail).text = model.address
        holder.itemView.findViewById<TextView>(R.id.item_estimated_receipt_phone_number).text = model.mobileNumber
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}