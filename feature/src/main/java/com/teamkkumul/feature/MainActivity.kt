package com.teamkkumul.feature

import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.snackBar
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<SearchViewModel>()

    private var _reqresAdapter: ReqresAdapter? = null
    private val reqresAdapter: ReqresAdapter
        get() = requireNotNull(_reqresAdapter)

    override fun initView() {
        initReqresRecyclerView()
        initObserveReqresState()
    }

    private fun initReqresRecyclerView() {
        _reqresAdapter = ReqresAdapter().apply {
            submitList(emptyList())
        }
        binding.rvReqres.adapter = reqresAdapter
    }

    private fun initObserveReqresState() {
        viewModel.reqresUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> reqresAdapter.submitList(state.data)
                is UiState.Failure -> snackBar(binding.root, state.errorMessage)
                is UiState.Loading -> toast("로딩중")
                UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }
}
