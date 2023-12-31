package com.dayker.datagrapher.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_DataGrapher)
        setContentView(binding.root)
        setUpActionBar()


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemProfile -> {
                    auth = Firebase.auth
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navController.navigate(R.id.authorizationFragment)
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateToolbar(destination)
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

    private fun updateToolbar(destination: NavDestination) {
        when (destination.id) {
            R.id.homeFragment -> {
                binding.appBar.toolBarMain.visibility = View.VISIBLE
            }
            else -> {
                binding.appBar.toolBarMain.visibility = View.GONE
            }
        }
    }

}