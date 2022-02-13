package com.paulo.easyfood.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.paulo.easyfood.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigate = findViewById<BottomNavigationView>(R.id.btn_nav)
        val navController = Navigation.findNavController(this, R.id.host_fr)

        NavigationUI.setupWithNavController(bottomNavigate, navController)
    }
}