package com.example.mrsmuarestaurant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.model.Address
import kotlin.collections.ArrayList

class AdapterAddress(var data: ArrayList<Address>, private var listener: Listeners) : RecyclerView.Adapter<AdapterAddress.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvAddress = view.findViewById<TextView>(R.id.tv_address)
        val layout = view.findViewById<CardView>(R.id.layout)
        val rd = view.findViewById<RadioButton>(R.id.rd_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rd.isChecked = a.isSelected
        holder.tvName.text = a.name
        holder.tvPhone.text = a.phone
        holder.tvAddress.text =  a.address

        holder.rd.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }

        holder.layout.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }
    }

    interface Listeners {
        fun onClicked(data: Address)
    }

}