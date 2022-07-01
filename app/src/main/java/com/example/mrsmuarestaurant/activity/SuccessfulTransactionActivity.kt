package com.example.mrsmuarestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mrsmuarestaurant.databinding.ActivitySuccessfulTransactionBinding

class SuccessfulTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessfulTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessfulTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainButton()
    }

    private fun mainButton(){
        binding.btnDetailTransaction.setOnClickListener {
            startActivity(Intent(this, HistoryTransactionActivity::class.java))
        }

        binding.btnGohome.setOnClickListener {
            startActivity(Intent(this, BottomBarActivity::class.java))
        }
    }
}