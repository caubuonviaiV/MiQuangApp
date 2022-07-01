package com.example.mrsmuarestaurant.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.Checkout
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.room.MyDatabase
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)
        Helper().setToolbar(this, toolbar, "Thanh toán")
        myDb = MyDatabase.getInstance(this)!!

        totalPrice()
        mainButton()
        setDeliveryMethod()
    }

    var totalPrice = 0
    var shipPrice = 10000
    private fun totalPrice() {
        totalPrice = Integer.valueOf(intent.getStringExtra("extra")!!)
        tv_totalCart.text = Helper().convertToVN(totalPrice)
        tv_ship.text = Helper().convertToVN(shipPrice)
        tv_total.text = Helper().convertToVN(totalPrice + shipPrice)
    }

    private fun setDeliveryMethod() {
        val arrayString = ArrayList<String>()
        arrayString.add("Giao hàng tận nơi")
        arrayString.add("Mua hàng mang về")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arrayString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_method.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    fun checkAddress() {

        if (myDb.daoAddress().getByStatus(true) != null) {
            div_address.visibility = View.VISIBLE
            div_empty.visibility = View.GONE
            div_Shippingmethod.visibility = View.VISIBLE

            val a = myDb.daoAddress().getByStatus(true)!!
            tv_name.text = a.name
            tv_phone.text = a.phone

            tv_address.text = a.address
            btn_addAddress.text = "Thay đổi địa chỉ"
        } else {
            div_address.visibility = View.GONE
            div_empty.visibility = View.VISIBLE
            btn_addAddress.text = "Thêm địa chỉ"
        }
    }

    private fun mainButton() {
        btn_addAddress.setOnClickListener {
            startActivity(Intent(this, ListAddressActivity::class.java))
        }

        btn_done.setOnClickListener {
            pay()
        }
    }

    private fun pay() {
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAddress().getByStatus(true)!!

        val listProduct = myDb.daoCart().getAll() as ArrayList
        var totalItem = 0
        var totalPrice = 0
        val products = ArrayList<Checkout.Cart>()
        for (p in listProduct) {
            if (p.selected) {
                totalItem += p.total
                totalPrice += (p.total * Integer.valueOf(p.price_sale))

                val product = Checkout.Cart()
                product.id = "" + p.id
                product.total_item = "" + p.total
                product.total_price = "" + (p.total * Integer.valueOf(p.price_sale))
                products.add(product)
            }
        }

        val checkout = Checkout()
        checkout.user_id = "" + user.id
        checkout.total_item = "" + totalItem
        checkout.total_price = "" + totalPrice
        checkout.name = a.name
        checkout.email = "" + user.email
        checkout.address = a.address
        checkout.phone = a.phone
        checkout.content = edt_content.text.toString()
        checkout.payment_type = "pay_later"
        checkout.status = "DEFAULT"
        checkout.method = spn_method.selectedItem.toString()
        checkout.products = products

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@DeliveryActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                if (respon.success == 1) {
                    Toast.makeText(this@DeliveryActivity,"Đặt món ăn thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DeliveryActivity,   "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        //delete Cart
        val myDb = MyDatabase.getInstance(this)!!
        for (product in checkout.products) {
            myDb.daoCart().deleteById(product.id)
        }

        val intent = Intent(this, SuccessfulTransactionActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAddress()
        super.onResume()
    }
}
