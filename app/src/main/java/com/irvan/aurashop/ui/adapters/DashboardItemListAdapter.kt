package com.irvan.aurashop.ui.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.irvan.aurashop.R
import com.irvan.aurashop.models.Product
import com.irvan.aurashop.ui.activities.ProductDetailsActivity
import com.irvan.aurashop.utils.Constant
import com.irvan.aurashop.utils.GlideLoader

open class DashboardItemListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder (
            LayoutInflater.from(context).inflate(
                    R.layout.item_dashboard_layout,
                    parent,
                    false)
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(
                    model.product_image,
                    holder.itemView.findViewById(R.id.item_dashboard_layout_imageview)
            )
            holder.itemView.findViewById<TextView>(R.id.item_dashboard_item_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.item_dashboard_item_price).text = "Rp ${model.product_price}"

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,model.user_id) //EXTRA_PRODUCT_OWNER_ID adalah mendapatkan id user ketika user menambahkan produk tersebut ke dalam cart / keranjang,
                context.startActivity(intent)

            }

//            holder.itemView.setOnClickListener {
//                if (onClickListener != null){
//                    onClickListener!!.onClick(position,model)
//                }
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //fungsi untuk ketika mengklik detail produk langsung dari antarmuka / tampilan dashboard
    interface OnClickListener{
        fun onClick(position: Int, product: Product)
    }

}