package com.example.mrsmuarestaurant.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.activity.DeliveryActivity
import com.example.mrsmuarestaurant.activity.StartScreenActivity
import com.example.mrsmuarestaurant.adapter.AdapterCart
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.Product
import com.example.mrsmuarestaurant.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {

    private lateinit var myDb: MyDatabase
    private lateinit var s: SharedPref

    // được gọi một lần khi hoạt động đang hoạt động
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_cart, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())

        mainButton()
        return view
    }

    lateinit var adapter: AdapterCart
    var listProduct = ArrayList<Product>()

    private fun displayProduct() {
        listProduct = myDb.daoCart().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterCart(requireActivity(), listProduct, object : AdapterCart.Listeners {
            override fun onUpdate() {
                countTotal()
            }

            override fun onDelete(position: Int) {
                listProduct.removeAt(position)
                adapter.notifyDataSetChanged()
                countTotal()
            }
        })
        rvProduct.adapter = adapter
        rvProduct.layoutManager = layoutManager
    }

    private var totalPrice = 0
    private var quantityItem = 0
    fun countTotal() {
        val listProduct = myDb.daoCart().getAll() as ArrayList
        totalPrice = 0
        quantityItem = 0

        var isSelectedAll = true
        for (product in listProduct) {
            if (product.selected) {
                val price = Integer.valueOf(product.price_sale)
                totalPrice += (price * product.total)

                quantityItem += Integer.valueOf(product.total)
            } else {
                isSelectedAll = false
            }
        }
        cbAll.isChecked = isSelectedAll
        tvTotal.text = Helper().convertToVN(totalPrice)
    }

    private fun mainButton() {
        btnDelete.setOnClickListener {
            val listDelete = ArrayList<Product>()
            for (p in listProduct) {
                if (p.selected) listDelete.add(p)
            }

            delete(listDelete)
            countTotal()
        }

        btnToCheckout.setOnClickListener {

            if (s.getStatusLogin()) {
                var isThereProduct = false
                for (p in listProduct) {
                    if (p.selected) isThereProduct = true
                }

                if (isThereProduct) {
                    val intent = Intent(requireActivity(), DeliveryActivity::class.java)
                    intent.putExtra("extra", ""+ totalPrice)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Chưa có món nào trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                requireActivity().startActivity(Intent(requireActivity(), StartScreenActivity::class.java))
            }
        }

        cbAll.setOnClickListener {
            for (i in listProduct.indices) {
                val product = listProduct[i]
                product.selected = cbAll.isChecked
                listProduct[i] = product
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun delete(data: ArrayList<Product>) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoCart().delete(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listProduct.clear()
                    listProduct.addAll(myDb.daoCart().getAll() as ArrayList)
                    adapter.notifyDataSetChanged()
                })
    }

    private lateinit var btnDelete: ImageView
    private lateinit var rvProduct: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnToCheckout: TextView
    private lateinit var cbAll: CheckBox


    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduct = view.findViewById(R.id.rv_product)
        tvTotal = view.findViewById(R.id.tv_total)
        btnToCheckout = view.findViewById(R.id.to_checkout)
        cbAll = view.findViewById(R.id.cb_all)
    }

    override fun onResume() {
        displayProduct()
        countTotal()
        super.onResume()
    }
}
