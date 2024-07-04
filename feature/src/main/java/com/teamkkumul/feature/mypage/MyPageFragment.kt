package com.teamkkumul.feature.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.snackBar
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val viewModel by viewModels<MyPageViewModel>()
    override fun initView() {
        binding.btnMyPage.setOnClickListener {
            viewModel.saveUserToken(binding.etMyPage.text.toString())
        }

        binding.btnGetToken.setOnClickListener {
            viewModel.getUserToken()
            initObserve()
        }

        binding.btnClear.setOnClickListener {
            viewModel.deleteToken()
        }
    }

    private fun initObserve() {
        viewModel.userInfoState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> snackBar(binding.root, it.data)
                is UiState.Failure -> snackBar(binding.root, it.errorMessage)
                is UiState.Loading -> snackBar(binding.root, "Loading")
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }
}
