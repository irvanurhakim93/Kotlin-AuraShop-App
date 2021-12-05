package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Orders
import com.irvan.aurashop.ui.activities.OrdersDetailsActivity
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

open class OrdersListAdapter (
        private val context: Context,
        private var list:ArrayList<Orders>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_list_layout,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(
                    model.product_image,
                    holder.itemView.findViewById(R.id.image_view_item_image)
            )

            holder.itemView.findViewById<TextView>(R.id.item_list_product_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.item_list_product_price).text = "RP${model.total_amount}"

            holder.itemView.setOnClickListener {
                val intent = Intent(context, OrdersDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_MY_ORDER_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}