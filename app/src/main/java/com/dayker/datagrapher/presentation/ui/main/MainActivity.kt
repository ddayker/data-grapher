package com.dayker.datagrapher.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_DataGrapher)
        setContentView(binding.root)
        setUpActionBar()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemProfile -> {
                    Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    private fun setUpActionBar() {
        binding.appBar.toolBar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}