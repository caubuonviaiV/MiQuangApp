package com.example.mrsmuarestaurant.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.model.Slider
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.squareup.picasso.Picasso
import java.util.ArrayList


class AdapterSlider(private var data: ArrayList<Slider>): RecyclerView.Adapter<AdapterSlider.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSlider: ImageView = view.findViewById(R.id.image)
        val name: TextView = view.findViewById(R.id.tv_decrip)
        val button : TextView = view.findViewById(R.id.tv_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return AdapterSlider.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(data[position].name, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(data[position].name)
        }

        holder.button.text = data[position].button

        val image = ConfigIMG.productUrl + data[position].thumb
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .into(holder.imgSlider)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}

