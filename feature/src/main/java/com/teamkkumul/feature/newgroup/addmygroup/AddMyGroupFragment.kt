package com.teamkkumul.feature.newgroup.addmygroup

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentAddMyGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMyGroupFragment : BindingFragment<FragmentAddMyGroupBinding>(R.layout.fragment_add_my_group) {
    override fun initView() {
        binding.ivBtnEnterInvitationCode.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_my_group_to_fragment_enter_invitation_code)
        }
        binding.ivBtnAddNewGroup.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_my_group_to_fragment_add_new_group)
        }
    }
}
