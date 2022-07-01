package com.example.mrsmuarestaurant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.DetailTransaction
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterProductTransaction(var data: ArrayList<DetailTransaction>) : RecyclerView.Adapter<AdapterProductTransaction.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val tvTotalPrice = view.findViewById<TextView>(R.id.tv_totalPrice)
        val tvAmount = view.findViewById<TextView>(R.id.tv_number)

        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_product, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val name = a.product.name
        val p = a.product

        holder.tvName.text = name
        holder.tvPrice.text = Helper().convertToVN(p.price_sale)
        holder.tvTotalPrice.text = Helper().convertToVN(a.total_price)
        holder.tvAmount.text = a.total_item + " Món"

        holder.layout.setOnClickListener {
//            listener.onClicked(a)
        }

        val image = ConfigIMG.productUrl + p.thumb
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(holder.imgProduct)
    }

    interface Listeners {
        fun onClicked(data: DetailTransaction)
    }

}