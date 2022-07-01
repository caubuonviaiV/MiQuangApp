package com.example.mrsmuarestaurant.fragment


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.adapter.AdapterNoodle
import com.example.mrsmuarestaurant.model.Product
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.adapter.AdapterProduct
import com.example.mrsmuarestaurant.adapter.AdapterSlider
import com.example.mrsmuarestaurant.model.Slider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    val phoneOrder = "99999999999"
    val phoneReservation = "1111111111"
    private lateinit var vpSlider: RecyclerView
    private lateinit var rvProduct: RecyclerView
    private lateinit var rvNoodle: RecyclerView

    private lateinit var bt_order: Button
    private lateinit var bt_reservation : Button
    private lateinit var viewcart : RelativeLayout
    val REQUEST_PHONE_CALL = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)


        getProduct()
        mainbuton()
        getSlider()
        getNoodle()
        return view
    }

    private fun mainbuton() {
        bt_reservation.setOnClickListener() {
            if (ActivityCompat.checkSelfPermission(getContext()as Activity, android.Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getContext() as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            } else {
                startCallReservation()
            }
        }
        bt_order.setOnClickListener() {
            if (ActivityCompat.checkSelfPermission(getContext()as Activity, android.Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getContext() as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            } else {
                startCallOrder()
            }

        }

        viewcart.setOnClickListener() {
           startActivity(Intent(requireActivity(), CartFragment::class.java))
        }
    }

    private fun startCallOrder() {
        val cal = Intent(Intent.ACTION_CALL)

        cal.data = Uri.parse("tel:" + phoneOrder)
        startActivity(cal)
    }
    private fun startCallReservation() {
        val cal = Intent(Intent.ACTION_CALL)

        cal.data = Uri.parse("tel:" + phoneReservation)
        startActivity(cal)
    }

    fun displayProduct() {
        Log.d("Check", "size:" + listProduct.size)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rvProduct.adapter = AdapterProduct(requireActivity(),listProduct)
        rvProduct.layoutManager = layoutManager
    }

    fun displaySlider() {
        Log.d("Check", "size:" + listSlider.size)

        val layoutManager1 = LinearLayoutManager(activity)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL

        vpSlider.adapter = AdapterSlider(listSlider)
        vpSlider.layoutManager = layoutManager1
    }

    fun displayNoodle() {
        Log.d("Check", "size:" + listNoodle.size)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rvNoodle.adapter = AdapterNoodle(requireActivity(),listNoodle)
        rvNoodle.layoutManager = layoutManager
    }

    private var listProduct: ArrayList<Product> = ArrayList()
    private var listSlider: ArrayList<Slider> = ArrayList()
    private var listNoodle: ArrayList<Product> = ArrayList()

    private fun getProduct() {
        ApiConfig.instanceRetrofit.getProduct().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                val a = 1;
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduct = ArrayList<Product>()
                    for (p in res.product) {
                        arrayProduct.add(p)
                    }
                    listProduct = arrayProduct

                    listProduct = res.product
                    displayProduct()
                }
            }
        })
    }

    private fun getSlider() {
        ApiConfig.instanceRetrofit.getSlider().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                val a = 1;
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arraySlider = ArrayList<Slider>()
                    for (p in res.slider) {
                        arraySlider.add(p)
                    }
                    listSlider = arraySlider

                    listSlider = res.slider
                    displaySlider()
                }
            }
        })
    }

    private fun getNoodle() {
        ApiConfig.instanceRetrofit.getNoodles().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                val a = 1;
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayNoodle = ArrayList<Product>()
                    for (p in res.product) {
                        arrayNoodle.add(p)
                    }
                    listNoodle = arrayNoodle

                    listNoodle = res.product
                    displayNoodle()
                }
            }
        })
    }

    private fun init(view: View) {
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduct = view.findViewById(R.id.rv_product)
        rvNoodle = view.findViewById(R.id.rv_noodles)
        bt_order = view.findViewById(R.id.bt_ordercall)
        bt_reservation = view.findViewById(R.id.bt_resevaltioncall)
        viewcart = view.findViewById(R.id.Tocart)
    }
}
