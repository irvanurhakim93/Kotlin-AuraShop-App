package com.irvan.aurashop.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Address
import com.irvan.aurashop.ui.activities.AddEditAddressActivity
import com.irvan.aurashop.ui.activities.CheckOutActivity
import com.irvan.aurashop.utils.Constant

open class AddressListAdapter (
    private val context:Context,
    private var list: ArrayList<Address>,
    private val selectAddress:Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_address_layout,
                        parent,
                        false
                )
        )
    }

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constant.EXTRA_ADDRESS_DETAILS, list[position])
        activity.startActivityForResult(intent, Constant.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.findViewById<TextView>(R.id.item_address_fullname).text = model.name
            holder.itemView.findViewById<TextView>(R.id.item_address_address_details).text = "${model.address}"
            holder.itemView.findViewById<TextView>(R.id.item_address_mobile_number).text = model.mobileNumber

            if (selectAddress){
                holder.itemView.setOnClickListener {

                    val intent = Intent(context, CheckOutActivity::class.java)
                    intent.putExtra(Constant.EXTRA_SELECTED_ADDRESS, model)
                    context.startActivity(intent)

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}