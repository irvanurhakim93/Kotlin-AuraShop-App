package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.activities.SeeDetailsBillsActivity

open class AfterConfrmedDeliveryAdapter (
        private val context:Context,
        private val list:ArrayList<Orders>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_after_confirmed_delivery,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.findViewById<TextView>(R.id.item_after_confirmed_delivery_detail_total_shop_rp).text=model.total_amount
        holder.itemView.findViewById<TextView>(R.id.item_after_confirmed_delivery_see_details_btn).setOnClickListener {
            val intent = Intent(context, SeeDetailsBillsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}