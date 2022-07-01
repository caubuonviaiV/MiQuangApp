package com.example.mrsmuarestaurant.activity


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.databinding.ActivityDetailPostBinding
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Post
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.toolbar.*

class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Helper().setToolbar(this, toolbar, "Chi tiết bài viết")
        getInfo()
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extraPost")
        post = Gson().fromJson(data, Post::class.java)

        // set Value
        binding.tvNameDtp.text = post.name

        val format = "dd/MM/yyyy"
        binding.tvCreatedAt.text =
            post.created_at?.let { Helper().convertDate(it, format) }


        binding.tvDescription.text = post.content
        binding.tvDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(post.content)
        }

        val img = ConfigIMG.productUrl + post.thumb
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .resize(400, 400)
            .into(binding.imgPost)
    }

}







