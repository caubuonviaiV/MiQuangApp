package com.example.mrsmuarestaurant.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mrsmuarestaurant.activity.AddAddressActivity
import com.example.mrsmuarestaurant.activity.HistoryTransactionActivity
import com.example.mrsmuarestaurant.activity.LoginActivity
import com.example.mrsmuarestaurant.databinding.FragmentSettingBinding
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.room.MyDatabase
import kotlinx.android.synthetic.main.activity_delivery.*


class SettingFragment : Fragment() {
    private lateinit var myDb: MyDatabase
    private var _binding: FragmentSettingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var s: SharedPref
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvId: TextView
    private lateinit var tvAdress: TextView
    private lateinit var logout: Button
    private lateinit var history: Button
    private lateinit var addAddress: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        s = SharedPref(this.activity as Activity);
        myDb = MyDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    override fun onStart() {
        super.onStart()
        setData()
        mainButton()
    }

    private fun mainButton() {
        logout.setOnClickListener {
            s.setStatusLogin(false)
        }

        history.setOnClickListener() {
            startActivity(Intent(requireActivity(), HistoryTransactionActivity::class.java))
        }

        addAddress.setOnClickListener() {
            startActivity(Intent(requireActivity(), AddAddressActivity::class.java))
        }

    }

    private fun setData() {
        val a = myDb.daoAddress().getByStatus(true)
        if (s.getUser() == null) {
            return
        }

        val user = s.getUser()!!
        tvId.text = user.id.toString()
        tvName.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
        if (a != null) {
            tvAdress.text = a.address
        }
//        tvAdress.text = user.address
    }

    private fun init(view: View) {
        tvId = binding.tvId
        tvName = binding.tvName
        tvEmail = binding.tvEmail
        tvPhone = binding.tvPhone
        tvAdress = binding.tvAddress
        history = binding.btnHistory
        addAddress = binding.toAddAddress

        logout = binding.btnLogout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}