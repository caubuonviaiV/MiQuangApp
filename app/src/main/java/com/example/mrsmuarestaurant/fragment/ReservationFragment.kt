package com.example.mrsmuarestaurant.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.mrsmuarestaurant.activity.BottomBarActivity
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.activity.StartScreenActivity
import com.example.mrsmuarestaurant.databinding.FragmentReservationBinding
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.ResponModel
import com.example.mrsmuarestaurant.room.MyDatabase
import kotlinx.android.synthetic.main.activity_add_address.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationFragment : Fragment() {
    private var _binding: FragmentReservationBinding? = null
    private lateinit var s: SharedPref
    private val binding get() = _binding!!

    private lateinit var Name: TextView
    private lateinit var Email: TextView
    private lateinit var Phone: TextView
    private lateinit var Date : CalendarView
    private lateinit var Time: TextView
    private lateinit var People: TextView
    private lateinit var Content: TextView

    private lateinit var btNext2: Button
    private lateinit var btNext3: Button
    private lateinit var btReturn1: Button
    private lateinit var btReturn2: Button
    private lateinit var btOrder: Button

    private lateinit var View1: CardView
    private lateinit var View2: CardView
    private lateinit var View3: CardView



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        val view: View = binding.root
        s = SharedPref(requireActivity())

        init(view)
        switchscreen()
        Reservation()
        setData()

        btOrder.setOnClickListener {
            Reservation()
        }
        return view
    }

    private fun setData() {
        if (s.getStatusLogin()) {
            if (s.getUser() == null) {
                return
            }

            val user = s.getUser()!!

            Name.setText(user.name).toString()
            Email.setText(user.email).toString()
            Phone.setText(user.phone).toString()
        }
//        else {
//            requireActivity().startActivity(Intent(requireActivity(), StartScreenActivity::class.java))
//        }

    }


    private fun Reservation() {
        when {
            binding.edtPeople.text.toString().isEmpty() -> {
                binding.edtPeople.error = "Số người không được để trống"
                binding.edtPeople.requestFocus()
                return
            }
            binding.edtTime.text.toString().isEmpty() -> {
                binding.edtTime.error = "Thời gian không được để trống"
                binding.edtTime.requestFocus()
                return
            }
            binding.calendarView3.date.toString().isEmpty() -> {
                binding.edtTime.error = "Ngày không được để trống"
                binding.edtTime.requestFocus()
                return
            }
            else ->
        ApiConfig.instanceRetrofit.reservation(
                binding.edtName.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtPhone.text.toString(),
                binding.edtContent.text.toString(),
                binding.calendarView3.date.toString(),
                binding.edtTime.text.toString(),
                binding.edtPeople.text.toString()
            ).enqueue(object : Callback<ResponModel> {

                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val response = response.body()!!
                    if (response.success == 1) {
                        val intent = Intent(getActivity(), BottomBarActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(getActivity(), "Đặt bàn thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(getActivity(), "Error:" + response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun switchscreen(){
         btNext2.setOnClickListener {
             binding.view1.visibility = View.GONE
             binding.view2.visibility = View.VISIBLE
             binding.view3.visibility = View.GONE
        }

        btNext3.setOnClickListener{
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.GONE
            binding.view3.visibility = View.VISIBLE
        }

        btReturn1.setOnClickListener{
            binding.view1.visibility = View.VISIBLE
            binding.view2.visibility = View.GONE
            binding.view3.visibility = View.GONE
        }
        btReturn2.setOnClickListener{
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.VISIBLE
            binding.view3.visibility = View.GONE
        }
    }


    private fun init(view: View) {
        Name = binding.edtName
        Email = binding.edtEmail
        Phone = binding.edtPhone
        People = binding.edtPeople
        Content = binding.edtContent
        Time = binding.edtTime
        Date = binding.calendarView3

        View1 = binding.view1
        View2 = binding.view2
        View3 = binding.view3

        btNext2= binding.btnNext2
        btReturn1= binding.btnReturn1
        btNext3= binding.btnNext3
        btReturn2= binding.btnReturn2
        btReturn2= binding.btnReturn2
        btOrder= binding.btnOrder

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}