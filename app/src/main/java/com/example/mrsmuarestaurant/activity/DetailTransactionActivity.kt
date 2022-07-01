package com.example.mrsmuarestaurant.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.adapter.AdapterProductTransaction
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.model.DetailTransaction
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.model.Transaction
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_transaction.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailTransactionActivity : AppCompatActivity() {

    private var transaction = Transaction()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction)
        Helper().setToolbar(this, toolbar, "Chi tiết đơn hàng")

        val json = intent.getStringExtra("transaction")
        transaction = Gson().fromJson(json, Transaction::class.java)

        setData(transaction)
        displayProduct(transaction.carts)
        mainButton()
    }

    private fun mainButton() {
        btn_cancel.setOnClickListener {
            cancelTransaction()
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Bạn có chắc không?")
                    .setContentText("Giao dịch sẽ bị hủy và không thể trả lại!")

                    .setConfirmText("Có, hủy bỏ đơn hàng")
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        cancelTransaction()
                    }

                    .setCancelText("đóng cửa sổ")
                    .setCancelClickListener {
                        it.dismissWithAnimation()
                    }.show()
        }
    }

    private fun cancelTransaction() {
        val loading = SweetAlertDialog(this@DetailTransactionActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("đang tải...").show()
        ApiConfig.instanceRetrofit.cancelCheckout(transaction.id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {
                    SweetAlertDialog(this@DetailTransactionActivity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Thành công...")
                            .setContentText("Đã hủy giao dịch thành công")
                            .setConfirmClickListener {
                                it.dismissWithAnimation()
                                onBackPressed()
                            }
                            .show()

//                    Toast.makeText(this@DetailTransactionActivity, "Đã hủy giao dịch thành công", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
                } else {
                    error(res.message)
                }
            }
        })
    }

    fun error(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("ối...")
                .setContentText(message)
                .show()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setData(transaction: Transaction) {
        tv_status.text = transaction.status
        val newformat = "dd/MM/yyyy - kk:mm"
        tv_tgl.text  = Helper().convertDate(transaction.created_at, newformat)
        tv_infor.text = "Tên và số điện thoại : " +transaction.name + " - " + transaction.phone
        tv_address.text = "Địa chỉ : " +transaction.address
        tv_totalShopping.text = transaction.total_item
        tv_total.text = Helper().convertToVN(transaction.total_price)

        val delivery = "Đang giao..."
        val done = "Đã giao"
        val canclled = "Đã hủy"
        if (transaction.status == "CANCELLED") {
            div_footer.visibility = View.GONE

            tv_status.text = canclled
            val color = getColor(R.color.bg_screen1)
            tv_status.setTextColor(color)
        }
        if (transaction.status == "DONE")  {
            tv_status.text = done
            val color = getColor(R.color.colorAccent)
            tv_status.setTextColor(color)
        }

        else if (transaction.status == "DEFAULT") {
            tv_status.text = delivery
            val color = getColor(R.color.colorPrimary)
            tv_status.setTextColor(color)
        }
    }

    private fun displayProduct(transaction: ArrayList<DetailTransaction>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_product.adapter = AdapterProductTransaction(transaction)
        rv_product.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
