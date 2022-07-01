package com.example.mrsmuarestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.adapter.AdapterHistory
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.model.Transaction
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history_transaction.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_transaction)
        Helper().setToolbar(this, toolbar, "Lịch sử đặt hàng")
    }

    private fun getHistory() {
        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getHistory(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    displayHistory(res.transactions)
                }
            }
        })
    }

    fun displayHistory(transactions: ArrayList<Transaction>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_history.adapter = AdapterHistory(transactions, object : AdapterHistory.Listeners {
            override fun onClicked(data: Transaction) {
                val json = Gson().toJson(data, Transaction::class.java)
                val intent = Intent(this@HistoryTransactionActivity, DetailTransactionActivity::class.java)
                intent.putExtra("transaction", json)
                startActivity(intent)
            }
        })
        rv_history.layoutManager = layoutManager
    }

    override fun onResume() {
        getHistory()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
