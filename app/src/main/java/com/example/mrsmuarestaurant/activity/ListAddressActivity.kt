package com.example.mrsmuarestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.adapter.AdapterAddress
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.Address
import com.example.mrsmuarestaurant.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_address.*
import kotlinx.android.synthetic.main.toolbar.*

class ListAddressActivity : AppCompatActivity() {
    lateinit var myDb : MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_address)
        Helper().setToolbar(this, toolbar, "Địa chỉ")

        myDb = MyDatabase.getInstance(this)!!

        mainButton()
    }

    private fun displayAddress() {
        val arrayList = myDb.daoAddress().getAll() as ArrayList

        if (arrayList.isEmpty()) div_empty.visibility = View.VISIBLE
        else div_empty.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_address.adapter = AdapterAddress(arrayList, object : AdapterAddress.Listeners {
            override fun onClicked(data: Address) {
                if (myDb.daoAddress().getByStatus(true) != null){
                    val addressActive = myDb.daoAddress().getByStatus(true)!!
                    addressActive.isSelected = false
                    updateActive(addressActive, data)
                }
            }
        })
        rv_address.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Address, dataNonActive: Address) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().update(dataActive) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateNonActive(dataNonActive)
                })
    }

    private fun updateNonActive(data: Address) {
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onBackPressed()
                })
    }

    override fun onResume() {
        displayAddress()
        super.onResume()
    }

    private fun mainButton() {
        btn_addAddress.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
