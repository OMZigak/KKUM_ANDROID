package com.teamkkumul.feature.mypage

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import coil.load
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyPageBinding
import com.teamkkumul.feature.utils.setEmptyImageUrl
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
        initUserName()
        setSpanText()
    }

    private fun initUserName() {
        viewModel.getLocalUserName()
        viewModel.userName.flowWithLifecycle(viewLifeCycle).onEach {
            binding.tvMyPageName.text = it
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMyPageState() {
        viewModel.myPageState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> binding.ivMyPageProfile.setEmptyImageUrl(it.data.profileImg)
                is UiState.Failure -> Timber.tag("my page").d(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setSpanText() {
        val textView = binding.tvMyPageUserState
        val fullText = getString(R.string.my_page_user_state)
        val spannableString = SpannableString(fullText)

        if (fullText.length >= 5) {
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_green)),
                0,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }

        textView.text = spannableString
    }
}
