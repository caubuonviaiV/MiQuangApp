package com.example.mrsmuarestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.helper.Helper
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.Address
import com.example.mrsmuarestaurant.model.ModelAddress
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressActivity : AppCompatActivity() {

    private var district = ModelAddress()
    private var ward = ModelAddress()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        Helper().setToolbar(this, toolbar, "Thêm địa chỉ")

        mainButton()
        getDistrict()
        setData()
    }

    private fun setData() {
        val user = SharedPref(this).getUser()!!

        edt_name.setText(user.name).toString()
        edt_phonenumber.setText(user.phone).toString()
    }

    private fun mainButton() {
        btn_save.setOnClickListener {
            save()
        }
    }

    private fun save() {
        when {
            edt_name.text.isEmpty() -> {
                error(edt_name)
                return
            }
            edt_phonenumber.text.isEmpty() -> {
                error(edt_phonenumber)
                return
            }
            (district.district_id == 0)-> {
                toast("Vui lòng chọn Quận/Huyện")
                return
            }

            edt_address.text.isEmpty() -> {
                error(edt_address)
                return
            }

        }

//        if (district.district_id == 0) {
//            toast("Vui lòng chọn Quận")
//            return
//        }
//
//        if (ward.ward_id == 0) {
//            toast("Vui lòng chọn phường")
//            return
//        }


        val address = Address()
        address.name = edt_name.text.toString()
        address.phone = edt_phonenumber.text.toString()

//        address.district_id = district.district_id
//        address.district = district.name
//
//        address.ward_id = ward.ward_id
//        address.ward = ward.name

        address.address = district.name + ", " + ward.name+ ", " +  edt_address.text.toString() + ""


        insert(address)
    }

    private fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText) {
        editText.error = "Không được để trống"
        editText.requestFocus()
    }

    private fun getDistrict() {
        ApiConfig.instanceRetrofit.getDistrict().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_district.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrayString = ArrayList<String>()
                    arrayString.add("Chọn Quận/Huyện")

                    val listDistricts = res.districts
                    for (district in listDistricts) {
                        arrayString.add(district.name)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAddressActivity,
                        R.layout.item_spinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_district.adapter = adapter
                    spn_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, district_id: Long) {
                            if (position != 0) {
                                district = listDistricts[position-1]
                                val idDistrict = listDistricts[position - 1].district_id
                                Log.d("respon", "Mã quận:" +idDistrict+" - " +"Tên quận:"+ listDistricts[position - 1].name)
                                getWard(idDistrict)
                            }
                        }
                    }

                } else {
                    Log.d("Error", "failed to load data:" + response.message())
               }
            }
        })
    }

    private fun getWard(ward_id: Int) {
        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.getWard(ward_id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_ward.visibility = View.VISIBLE

                    val res = response.body()!!

                    val arrayString = ArrayList<String>()
                    arrayString.add("Chọn Phường/Xã")

                    val listWard = res.wards
                    for (ward in listWard) {
                        arrayString.add(ward.name)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAddressActivity, R.layout.item_spinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_ward.adapter = adapter
                    spn_ward.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, ward_id: Long) {
                            if (position != 0) {
                                ward = listWard[position - 1]
                            }
                        }
                    }
                } else {
                    Log.d("Error", "failed to load data:" + response.message())
                }
            }
        })
    }


    private fun insert(data: Address) {
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAddress().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().insert(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toast("Thêm địa chỉ mới thành công")
                    onBackPressed()
                })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
