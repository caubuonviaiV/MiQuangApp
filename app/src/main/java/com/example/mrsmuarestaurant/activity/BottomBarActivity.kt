package com.example.mrsmuarestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.databinding.ActivityBottomBarBinding
import com.example.mrsmuarestaurant.fragment.*


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
//        val bottomNav : BottomNavigationView = binding.bottomNav
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

                R.id.menu -> {
                    currentFragment = MenuFragment()
                }

                R.id.order -> {
                    currentFragment = OrderFragment()
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