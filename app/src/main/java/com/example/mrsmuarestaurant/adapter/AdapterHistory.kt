package com.example.mrsmuarestaurant.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Transaction
import kotlin.collections.ArrayList

class AdapterHistory(var data: ArrayList<Transaction>, var listenner: Listeners) : RecyclerView.Adapter<AdapterHistory.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val tvDate = view.findViewById<TextView>(R.id.tv_tgl)
        val tvAmount = view.findViewById<TextView>(R.id.tv_number)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
        val btnDetail = view.findViewById<TextView>(R.id.btn_detail)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history_transaction, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]

        val idz = a.carts[0].id++
        holder.tvName.text = "Hóa Đơn #" +idz
        holder.tvPrice.text = Helper().convertToVN(a.total_price)
        holder.tvAmount.text = a.total_item + " Món"


        // 2021-04-30 18:30:20 //24
        // jam 1   k || 01  kk
        // 09:20:20 am 12/pm/am

        val newformat = "dd/MM/yyyy - kk:mm"
        holder.tvDate.text = Helper().convertDate(a.created_at, newformat)
//        holder.tvDate.text = a.created_at

        val delivery = "Đang giao..."
        val done = "Đã giao"
        val canclled = "Đã hủy"

        var color = context.getColor(R.color.colorPrimaryDark)
        if (a.status == "DONE") {
            holder.tvStatus.text = done
            color = context.getColor(R.color.colorAccent)
        }
        else if(a.status == "DEFAULT") {
            holder.tvStatus.text = delivery
            color = context.getColor(R.color.colorPrimary)
        }
        else if(a.status == "CANCELLED") {
            holder.tvStatus.text = canclled
            color = context.getColor(R.color.bg_screen1)
        }


        holder.tvStatus.setTextColor(color)

        holder.layout.setOnClickListener {
            listenner.onClicked(a)
        }
    }

    interface Listeners {
        fun onClicked(data: Transaction)
    }

}