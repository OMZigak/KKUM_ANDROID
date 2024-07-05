package com.teamkkumul.feature

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.feature.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_home) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvHome.setupWithNavController(navController)
        setBottomNaviVisible(navController)
        setToolBar(navController)
    }

    private fun setBottomNaviVisible(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvHome.visibility = when (destination.id) {
                R.id.fragment_home, R.id.fragment_my_group, R.id.fragment_my_page -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun setToolBar(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
            when (destination.id) {
                R.id.fragment_home -> binding.toolbar.visibility = View.GONE
                R.id.fragment_my_group, R.id.fragment_my_page -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.text = destination.label
                }
                else -> binding.toolbar.visibility = View.VISIBLE
            }
        }
    }
}
