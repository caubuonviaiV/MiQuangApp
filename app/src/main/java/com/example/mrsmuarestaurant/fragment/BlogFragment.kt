package com.example.mrsmuarestaurant.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.adapter.AdapterPost
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.model.Post
import com.example.mrsmuarestaurant.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BlogFragment : Fragment() {
    private lateinit var rvPost: RecyclerView


    override fun onCreateView(  inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_blog, container, false)
        init(view)

        getPost()

        return view
    }


    fun displayPost() {
        Log.d("Check", "size:" + listPost.size)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rvPost.adapter = AdapterPost(requireActivity(),listPost)
        rvPost.layoutManager = layoutManager
    }

    private var listPost: ArrayList<Post> = ArrayList()

    private fun getPost() {
        ApiConfig.instanceRetrofit.getPost().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                val a = 1;
            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayPost = ArrayList<Post>()
                    for (p in res.post) {
                        arrayPost.add(p)
                    }
                    listPost = arrayPost

                    listPost = res.post
                    displayPost()
                }
            }
        })
    }

    private fun init(view: View) {
        rvPost = view.findViewById(R.id.rv_post)
    }

}