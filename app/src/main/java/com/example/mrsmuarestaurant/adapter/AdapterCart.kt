package com.example.mrsmuarestaurant.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Product
import com.example.mrsmuarestaurant.room.MyDatabase
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AdapterCart(var activity: Activity, var data: ArrayList<Product>, var listener: Listeners)
    : RecyclerView.Adapter<AdapterCart.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val imgProduct = view.findViewById<ImageView>(R.id.img_productC)
        val layout = view.findViewById<CardView>(R.id.layout)

        val btnAdd = view.findViewById<ImageView>(R.id.btn_add)
        val btnLess = view.findViewById<ImageView>(R.id.btn_less)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvAmount = view.findViewById<TextView>(R.id.tv_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val product = data[position]
        val price = Integer.valueOf(product.price_sale)

//        val rating = Integer.valueOf(product.total_rating)
//        val number = Integer.valueOf(product.total_number)
//        holder.rating.text = Integer.valueOf(rating / number).toString()

        holder.tvName.text = product.name
        holder.tvPrice.text = Helper().convertToVN(price * product.total)

        var total = data[position].total
        holder.tvAmount.text = total.toString()

        holder.checkBox.isChecked = product.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            product.selected = isChecked
            update(product)
        }

        val image = ConfigIMG.productUrl + data[position].thumb
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(holder.imgProduct)


        holder.btnAdd.setOnClickListener {
            total++
            product.total = total
            update(product)

            holder.tvAmount.text = total.toString()
            holder.tvPrice.text = Helper().convertToVN(price * total)
        }

        holder.btnLess.setOnClickListener {
            if (total <= 1) return@setOnClickListener
            total--

            product.total = total
            update(product)

            holder.tvAmount.text = total.toString()
            holder.tvPrice.text = Helper().convertToVN(price * total)
        }

        holder.btnDelete.setOnClickListener {
            delete(product)
            listener.onDelete(position)
        }
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Product) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoCart().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listener.onUpdate()
                })
    }

    private fun delete(data: Product) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoCart().delete(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                })
    }

}