package com.example.mrsmuarestaurant.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.fragment.CartFragment
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Product
import com.example.mrsmuarestaurant.room.MyDatabase
import com.example.mrsmuarestaurant.Api.ConfigIMG
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_custom.*

class DetailProductActivity : AppCompatActivity() {
    private lateinit var myDb: MyDatabase
    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        myDb = MyDatabase.getInstance(this)!! // call database

        getInfo()
        mainButton()
        checkCart()
    }

    private fun mainButton() {

        btn_addToCart.setOnClickListener {
            val data = myDb.daoCart().getProduct(product.id)
             if (data == null) {
                 insert()
             } else {
                 data.total += 1
                 update(data)
             }
         }

        ToCart.setOnClickListener() {
            startActivity(Intent(this, CartFragment::class.java))
        }

        btn_Cart.setOnClickListener {
            val intent = Intent("event:cart")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }


    private fun insert() {
        CompositeDisposable().add(Observable.fromCallable{ myDb.daoCart().insert(product) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkCart()
                    Log.d("response", "dữ liệu được chèn vào!")
                    Toast.makeText(this, "Đã thêm thành công vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                })
    }

    private fun update(data: Product) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoCart().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkCart()
                    Log.d("response", "đã sửa dữ liệu!")
                    Toast.makeText(this, "Đã thêm thành công vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                })
    }

    private fun checkCart() {
        val dataCart = myDb.daoCart().getAll()

        if (dataCart.isNotEmpty()) {
            div_number.visibility = View.VISIBLE
            tv_number.text = dataCart.size.toString()
        } else {
            div_number.visibility = View.GONE
        }
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        product = Gson().fromJson<Product>(data, Product::class.java)

         // set Value
        tv_name.text = product.name
        tv_Price.text = Helper().convertToVN(product.price_sale)
        tv_Description.text = product.content

        val img = ConfigIMG.productUrl + product.thumb
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .resize(400, 240)
                .into(image)

        // setToolbar
        Helper().setToolbar(this, toolbar, product.name)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}


