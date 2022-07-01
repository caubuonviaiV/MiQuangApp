package com.example.mrsmuarestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.databinding.ActivityStartScreenBinding
import com.example.mrsmuarestaurant.fragment.HomeFragment
import com.example.mrsmuarestaurant.helper.SharedPref

class StartScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainButton()
    }

    private fun mainButton(){
        binding.btnProsesLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnGohome.setOnClickListener {
            startActivity(Intent(this, BottomBarActivity::class.java))
        }
    }
}