package com.teamkkumul.feature.mygroup

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupFragment : BindingFragment<FragmentMyGroupBinding>(R.layout.fragment_my_group) {

    override fun initView() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_myGroupFragment_to_myGroupDetailFragment)
        }
    }
}
