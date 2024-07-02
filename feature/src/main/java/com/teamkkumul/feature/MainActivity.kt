package com.teamkkumul.feature

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initObserveReqresState()
    }

    private fun initObserveReqresState() {
        viewModel.reqresUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> toast("성공")
                is UiState.Failure -> toast("실패")
                is UiState.Loading -> toast("로딩중")
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}
