package com.teamkkumul.feature

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.feature.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_home) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvHome.setupWithNavController(navController)
    }
}
