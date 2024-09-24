package com.teamkkumul.feature.utils.bottomsheet

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingBottomSheetFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExitBottomSheetBinding
import com.teamkkumul.feature.utils.KeyStorage.GROUP_NAME
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.type.DeleteDialogType

class MyGroupExitBottomSheetFragment :
    BindingBottomSheetFragment<FragmentExitBottomSheetBinding>(R.layout.fragment_exit_bottom_sheet) {
    private val currentId: Int by lazy { arguments?.getInt(MEETING_ID, -1) ?: -1 }
    private val groupName: String by lazy { arguments?.getString(GROUP_NAME).orEmpty() }

    override fun initView() {
        myGroupPlusButton()
    }

    private fun myGroupPlusButton() {
        binding.tvGroupName.text = groupName
        binding.tvExitBackground.setOnClickListener {
            val action = MyGroupExitBottomSheetFragmentDirections.actionToDeleteDialogFragment(
                dialogType = DeleteDialogType.MY_GROUP_LEAVE_DIALOG,
                meetingId = currentId,
            )
            findNavController().navigate(action)
        }

        binding.tvCancleBackground.setOnClickListener {
            dismiss()
        }
    }
}
