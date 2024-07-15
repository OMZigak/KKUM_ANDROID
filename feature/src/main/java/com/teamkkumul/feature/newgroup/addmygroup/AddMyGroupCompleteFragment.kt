package com.teamkkumul.feature.newgroup.addmygroup

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentAddMyGroupCompleteBinding

class AddMyGroupCompleteFragment :
    BindingFragment<FragmentAddMyGroupCompleteBinding>(R.layout.fragment_add_my_group_complete) {
    override fun initView() {
        binding.btnOkay.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_my_group_complete_to_fragment_my_group_detail)
        }
    }
}
