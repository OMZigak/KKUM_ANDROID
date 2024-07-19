package com.teamkkumul.feature

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.statusBarColorOf
import com.teamkkumul.feature.databinding.ActivityMainBinding
import com.teamkkumul.feature.utils.KeyStorage
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
        setBottomNaviVisible(navController)
        updateStatusBarColor(navController)
//        navigateToMeetUpContainer(navController)
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

    private fun navigateToMeetUpContainer(navController: NavController) {
        intent?.action?.let { action ->
            if (action == "android.intent.action.MAIN") {
                navController.navigate(
                    R.id.fragment_meet_up_container,
                    bundleOf(KeyStorage.TAB_INDEX to 1),
                )
            }
        }
    }
}
