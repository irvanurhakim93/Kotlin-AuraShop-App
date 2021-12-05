package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.WishlistItem
import com.irvan.aurashop.ui.activities.CartListActivity
import com.irvan.aurashop.ui.activities.ProductDetailsActivity
import com.irvan.aurashop.ui.activities.WishlistActivity
import com.irvan.aurashop.utils.AuraShopButton
import com.irvan.aurashop.utils.GlideLoader

open class WishListItemsListAdapter(
    private val context: Context,
    private var list:ArrayList<WishlistItem>,
    private val updateWishListItem: Boolean
) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_wishlist_layout,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.product_image, holder.itemView.findViewById(R.id.wishlist_layout_imageview))
            holder.itemView.findViewById<TextView>(R.id.wishlist_layout_item_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.wishlist_item_price).text = "RP.${model.product_price}"
        }

        holder.itemView.findViewById<ImageButton>(R.id.item_wishlist_layout_delete_btn).setOnClickListener {
            when(context){
                is WishlistActivity -> {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait_msg))
                }
            }
            FirestoreClass().removeItemFromWishlist(context, model.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}




