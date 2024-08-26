package com.teamkkumul.feature.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyPageBinding
import com.teamkkumul.feature.utils.extension.updateLevelText
import com.teamkkumul.feature.utils.setEmptyImageUrl
import com.teamkkumul.feature.utils.type.DeleteDialogType
import com.teamkkumul.feature.utils.type.LevelColorType
import com.teamkkumul.model.home.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<MyPageViewModel>()

    override fun initView() {
        viewModel.getMyPageUserInfo()
        initObserveMyPageState()
        initLogoutClickListener()
        initWithdrawalClickListener()
    }

    private fun initObserveMyPageState() {
        viewModel.myPageState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> handleSuccess(it.data)
                is UiState.Failure -> Timber.tag("my page").d(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleSuccess(data: UserModel) {
        binding.tvMyPageName.text = data.name
        binding.ivMyPageProfile.setEmptyImageUrl(data.profileImg)
        binding.tvMyPageUserState.text =
            requireContext().updateLevelText(data.level, LevelColorType.MY_PAGE)
    }

    private fun initWithdrawalClickListener() {
        binding.tvMyPageWithdrawal.setOnClickListener {
            val action = MyPageFragmentDirections.actionFragmentMyPageToFragmentDeleteDialog(
                dialogType = DeleteDialogType.Withdrawal,
            )
            findNavController().navigate(action)
        }
    }

    private fun initLogoutClickListener() {
        binding.tvMyPageLogOut.setOnClickListener {
            val action = MyPageFragmentDirections.actionFragmentMyPageToFragmentDeleteDialog(
                dialogType = DeleteDialogType.Logout,
            )
            findNavController().navigate(action)
        }
    }
}
