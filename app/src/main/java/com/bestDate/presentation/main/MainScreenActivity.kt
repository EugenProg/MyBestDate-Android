package com.bestDate.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bestDate.R
import com.bestDate.databinding.ActivityMainScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isBottomVisible = destination.isBottomNavVisible()
            binding.bottomNavigationView.post {
                binding.bottomNavigationView.isVisible = isBottomVisible
            }
        }
        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}

fun NavDestination.isBottomNavVisible(): Boolean {
    return when (displayName.split(":").getOrNull(1)) {
        "id/searchFragment",
        "id/matchesFragment",
        "id/chatsFragment",
        "id/topFragment",
        "id/guestsFragment" -> true
        else -> false
    }
}