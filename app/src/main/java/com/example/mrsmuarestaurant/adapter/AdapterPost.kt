package com.example.mrsmuarestaurant.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.activity.DetailPostActivity
import com.example.mrsmuarestaurant.model.Post
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.example.mrsmuarestaurant.helper.Helper
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterPost(var activity: Activity, private var data: ArrayList<Post>)
    : RecyclerView.Adapter<AdapterPost.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView =  view.findViewById(R.id.tvp_name)
        val tvDescription: TextView  = view.findViewById(R.id.tv_description)
        val tvCreated_at: TextView  = view.findViewById(R.id.tv_created_at)
        val imgPost: ImageView = view.findViewById(R.id.img_post)

        val layout: CardView  = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvTitle.text = data[position].name
        holder.tvDescription.text = data[position].description


        val format = "dd/MM/yyyy"
        holder.tvCreated_at.text = data[position].created_at?.let { Helper().convertDate(it, format) }


        val image = ConfigIMG.productUrl + data[position].thumb
       Picasso.get()
               .load(image)
                .placeholder(R.drawable.product)
               .error(R.drawable.product)
               .into(holder.imgPost)

        holder.layout.setOnClickListener() {
            val activiti = Intent(activity, DetailPostActivity::class.java)
            //Toan bo sp thanh chuoi
            val str = Gson().toJson(data[position], Post::class.java)
            activiti.putExtra("extraPost", str)
            activity.startActivity(activiti)
        }
    }
}