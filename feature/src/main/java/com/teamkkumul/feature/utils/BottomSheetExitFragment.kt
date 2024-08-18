package com.teamkkumul.feature.utils

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamkkumul.core.ui.base.BindingBottomSheetFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExitBottomSheetBinding
import com.teamkkumul.feature.mygroup.mygroupdetail.MyGroupDetailViewModel
import com.teamkkumul.model.MyGroupInfoModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BottomSheetExitFragment :
    BindingBottomSheetFragment<FragmentExitBottomSheetBinding>(R.layout.fragment_exit_bottom_sheet) {
    private val viewModel: MyGroupDetailViewModel by viewModels()
    private var currentId: Int = -1

    override fun initView() {
        val id = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1

        if (id != -1) {
            currentId = id
        }

        viewModel.getMyGroupInfo(currentId)
        initObserveMyGroupInfoState()
    }

    private fun initObserveMyGroupInfoState() {
        viewModel.myGroupInfoState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    successMyGroupInfoState(uiState.data)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMyGroupInfoState(myGroupInfoModel: MyGroupInfoModel) {
        binding.tvGroupName.text = myGroupInfoModel.name
        binding.ivCancle.setOnClickListener{
            dismiss()
        }
    }
}
