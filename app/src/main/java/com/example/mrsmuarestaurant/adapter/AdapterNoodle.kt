package com.example.mrsmuarestaurant.adapter


import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.activity.DetailProductActivity
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Product
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterNoodle(var activity: Activity, private var data: ArrayList<Product>)
    : RecyclerView.Adapter<AdapterNoodle.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView =  view.findViewById(R.id.tv_name)
        val tvPrice: TextView  = view.findViewById(R.id.tv_price)
        val tvPriceSale: TextView  = view.findViewById(R.id.tv_price_sale)
        val imgProduct: ImageView = view.findViewById(R.id.img_product)

        val layout: ConstraintLayout = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_noodle, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]

        val price = Integer.valueOf(a.price)
        var PriceSale = Integer.valueOf(a.price_sale)

        holder.tvName.text = data[position].name
        holder.tvPriceSale.text = data[position].price_sale
        holder.tvPriceSale.text = Helper().convertToVN(PriceSale)

        holder.tvPrice.text = Helper().convertToVN(price)
        holder.tvPrice.paintFlags = holder.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
       val image = ConfigIMG.productUrl + data[position].thumb
       Picasso.get()
               .load(image)
                .placeholder(R.drawable.product)
               .error(R.drawable.product)
               .into(holder.imgProduct)

        holder.layout.setOnClickListener() {
            val activiti = Intent(activity, DetailProductActivity::class.java)
            //Toan bo sp thanh chuoi
            val str = Gson().toJson(data[position], Product::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }

    }
}