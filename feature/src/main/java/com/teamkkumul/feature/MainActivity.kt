package com.teamkkumul.feature

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.statusBarColorOf
import com.teamkkumul.feature.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        setupBottomNavigation()
        binding.clMain.setOnClickListener {
            hideKeyBoard()
        }
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_home) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvHome.setupWithNavController(navController)
        setBottomNaviVisible(navController)
        updateStatusBarColor(navController)
    }

    private fun setBottomNaviVisible(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvHome.visibility = when (destination.id) {
                R.id.fragment_home, R.id.fragment_my_group, R.id.fragment_my_page -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun updateStatusBarColor(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_home) {
                statusBarColorOf(R.color.main_color)
            } else {
                statusBarColorOf(R.color.white0)
            }
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
