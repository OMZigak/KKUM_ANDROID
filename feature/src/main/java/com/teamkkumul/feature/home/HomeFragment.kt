package com.teamkkumul.feature.home

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentHomeBinding

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun initView() {
        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_home_to_homeDetailFragment)
        }
    }
}
