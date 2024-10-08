package com.teamkkumul.feature.meetupcreate.location

import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.hideKeyboard
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateLocationBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateLocationViewModel
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpCreateLocationFragment :
    BindingFragment<FragmentMeetUpCreateLocationBinding>(R.layout.fragment_meet_up_create_location) {

    private val sharedViewModel: MeetUpSharedViewModel by activityViewModels<MeetUpSharedViewModel>()
    private val viewModel: MeetUpCreateLocationViewModel by viewModels<MeetUpCreateLocationViewModel>()
    private var _locationAdapter: MeetUpCreateLocationAdapter? = null
    private val locationAdapter get() = requireNotNull(_locationAdapter)

    override fun initView() {
        viewModel.setEmptyLocationList()
        initRecyclerView()
        initObserveMeetUpLocationState()
        updateNextButton(false)
        setupLocationNameEditorAction()
    }

    private fun setupLocationNameEditorAction() = with(binding.etMeetUpCreateLocationNameEnter) {
        setSingleLine(true)
        setOnEditorActionListener { _, actionId, event ->
            var handled = false
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
            ) {
                requireContext().hideKeyboard(this)
                val inputText = text.toString()
                viewModel.getMeetUpCreateLocation(inputText)
                handled = true
            }
            handled
        }
    }


    // 선택된 장소 업데이트
    private fun initRecyclerView() {
        _locationAdapter = MeetUpCreateLocationAdapter(
            onMeetUpCreateLocationSelected = { selectedItem ->
                sharedViewModel.updateMeetUpModel(
                    placeName = selectedItem.location,
                    x = selectedItem.x,
                    y = selectedItem.y,
                )
                updateNextButton(true)
                Timber.tag("dd").d(sharedViewModel.meetUpCreateModel.value.toString())
            },
        )
        binding.rvMeetUpCreateLocation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = locationAdapter
        }
    }

    private fun initObserveMeetUpLocationState() {
        viewModel.meetUpCreateLocationState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (uiState.data.isNotEmpty()) {
                        locationAdapter.submitList(uiState.data)
                    }
                }

                is UiState.Failure -> Timber.tag("meet up create location").d(uiState.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateNextButton(isEnabled: Boolean) {
        if (isEnabled) {
            with(binding) {
                tvNext.isEnabled = true
                tvNext.setOnClickListener {
                    findNavController().popBackStack(R.id.fragment_meet_up_create_location, true)
                }
            }
            ViewCompat.setBackgroundTintList(
                binding.tvNext,
                ContextCompat.getColorStateList(requireContext(), R.color.main_color),
            )
        } else {
            with(binding) {
                tvNext.isEnabled = false
                tvNext.setOnClickListener(null)
            }
            ViewCompat.setBackgroundTintList(
                binding.tvNext,
                ContextCompat.getColorStateList(requireContext(), R.color.gray2),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _locationAdapter = null
    }
}
