package com.example.mrsmuarestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.databinding.ActivityBottomBarBinding
import com.example.mrsmuarestaurant.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class BottomBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBarBinding

    private lateinit var currentFragment :Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomBarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //change laucher to home fragment
        supportFragmentManager.beginTransaction().replace(R.id.nav_container, HomeFragment()).commit()
        //set nav listener to bottom nav

        binding.bottomNav.setOnItemSelectedListener {
            //create fragment for current fragment
            when(it.itemId) {
                R.id.home -> {
                    currentFragment = HomeFragment()
                }
                R.id.blog -> {
                    currentFragment = BlogFragment()
                }
                R.id.cart -> {
                    currentFragment = CartFragment()
                }
                R.id.reservation -> {
                    currentFragment = ReservationFragment()
                }
                R.id.setting -> {
                    currentFragment = SettingFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.nav_container, currentFragment).commit()
            true
        }
    }
    //Create a navigation listener
}