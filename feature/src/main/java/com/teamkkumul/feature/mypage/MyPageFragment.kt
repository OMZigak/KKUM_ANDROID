package com.teamkkumul.feature.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.navigateToWeb
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyPageBinding
import com.teamkkumul.feature.signup.SetProfileActivity
import com.teamkkumul.feature.utils.KeyStorage.MY_PAGE_FRAGMENT
import com.teamkkumul.feature.utils.KeyStorage.SOURCE_FRAGMENT
import com.teamkkumul.feature.utils.WebLink
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
    private var profileImageUrl: String? = null

    override fun initView() {
        viewModel.getMyPageUserInfo()
        initObserveMyPageState()
        initLogoutClickListener()
        initWithdrawalClickListener()
        navigateToSetProfile()
        initUsePromiseClickListener()
        initAskClickListener()
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
        profileImageUrl = data.profileImg
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

    private fun navigateToSetProfile() {
        binding.ivMyPageProfile.setOnClickListener {
            val intent = Intent(requireContext(), SetProfileActivity::class.java).apply {
                putExtra(SetProfileActivity.PROFILE_IMAGE_URL, profileImageUrl)
                putExtra(SOURCE_FRAGMENT, MY_PAGE_FRAGMENT)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyPageUserInfo()
    }

    private fun initAskClickListener() {
        binding.tvMyPageAsk.setOnClickListener {
            requireContext().navigateToWeb(WebLink.GOOGLE_FORM_LINK)
        }
    }

    private fun initUsePromiseClickListener() {
        binding.tvMyPageUsePromise.setOnClickListener {
            requireContext().navigateToWeb(WebLink.NOTION_LINK)
        }
    }
}
