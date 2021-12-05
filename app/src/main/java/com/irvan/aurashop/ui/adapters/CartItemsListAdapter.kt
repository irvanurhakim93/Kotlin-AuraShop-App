package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.firestore.FirestoreClass
import com.irvan.aurashop.models.CartItem
import com.irvan.aurashop.ui.activities.CartListActivity
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

open class CartItemsListAdapter (
        private val context: Context,
        private var list: ArrayList<CartItem>,
        private val updateCartItem: Boolean
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_cart_layout,
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.product_image, holder.itemView.findViewById(R.id.cart_layout_imageview))
            holder.itemView.findViewById<TextView>(R.id.cart_layout_item_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.cart_item_price).text = "RP.${model.product_price}"
            holder.itemView.findViewById<TextView>(R.id.cart_layout_quantity_txt).text = model.cart_quantity

            //jika nilai cart quantity / jumlah keranjang 0 maka tombol fungsi menambah atau mengurangi jumlah item produk akan menghilang
            //hanya akan muncul tulisan "stok produk sedang habis"
            if(model.cart_quantity == "0"){
                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_remove_quantity_btn).visibility = View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_add_quantity_btn).visibility = View.GONE

                if (updateCartItem){
                    holder.itemView.findViewById<ImageButton>(R.id.item_cart_layout_delete_btn).visibility = View.VISIBLE
                } else {
                    holder.itemView.findViewById<ImageButton>(R.id.item_cart_layout_delete_btn).visibility = View.GONE
                }

                holder.itemView.findViewById<TextView>(R.id.cart_layout_quantity_txt).text = context.resources.getString(R.string.lbl_out_of_stock)
                holder.itemView.findViewById<TextView>(R.id.cart_layout_quantity_txt).setTextColor(
                        ContextCompat.getColor(context,R.color.snackBarError)
                )

                } else {

                    if (updateCartItem){
                        holder.itemView.findViewById<ImageButton>(R.id.cart_layout_remove_quantity_btn).visibility = View.VISIBLE
                        holder.itemView.findViewById<ImageButton>(R.id.cart_layout_add_quantity_btn).visibility = View.VISIBLE
                        holder.itemView.findViewById<ImageButton>(R.id.item_cart_layout_delete_btn).visibility = View.VISIBLE
                    } else {
                        holder.itemView.findViewById<ImageButton>(R.id.cart_layout_remove_quantity_btn).visibility = View.GONE
                        holder.itemView.findViewById<ImageButton>(R.id.cart_layout_add_quantity_btn).visibility = View.GONE
                        holder.itemView.findViewById<ImageButton>(R.id.item_cart_layout_delete_btn).visibility = View.GONE
                    }


                //terkecuali jika niilai cart quantity terisi maka tombol fungsi menambah atau mengurangi prouk akan muncul

                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_remove_quantity_btn).visibility = View.VISIBLE
                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_add_quantity_btn).visibility = View.VISIBLE

                holder.itemView.findViewById<TextView>(R.id.cart_layout_quantity_txt).setTextColor(
                        ContextCompat.getColor(context,R.color.dark_gray)
                )

                }

                holder.itemView.findViewById<ImageButton>(R.id.item_cart_layout_delete_btn).setOnClickListener {
                    when(context){
                        is CartListActivity -> {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait_msg))
                        }
                    }
                    FirestoreClass().removeItemFromCart(context, model.id)
                }

                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_remove_quantity_btn).setOnClickListener {
                    if (model.cart_quantity == "1"){
                        FirestoreClass().removeItemFromCart(context,model.id)
                    } else{
                        val cartQuantity: Int = model.cart_quantity.toInt()
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[Constant.CART_QUANTITY] = (cartQuantity - 1).toString()

                        //memunculkan progress dialog
                        if (context is CartListActivity){
                            context.showProgressDialog(context.resources.getString(R.string.please_wait_msg))
                        }

                        FirestoreClass().updateCart(context, model.id, itemHashMap)
                    }
                }

                holder.itemView.findViewById<ImageButton>(R.id.cart_layout_add_quantity_btn).setOnClickListener {
                    val cartQuantity: Int = model.cart_quantity.toInt()
                    if (cartQuantity < model.stock_quantity.toInt()){
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[Constant.CART_QUANTITY] = (cartQuantity + 1).toString()

                        if (context is CartListActivity){
                            context.showProgressDialog(context.resources.getString(R.string.please_wait_msg))
                        }
                        FirestoreClass().updateCart(context, model.id, itemHashMap)
                    } else {
                        if (context is CartListActivity){
                            context.showErrorSnackBar(
                                    context.resources.getString(R.string.msg_for_available_stock,model.stock_quantity),true
                            )
                        }
                    }
                }

            }
        }
    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)

}


