package com.teamkkumul.feature.meetupcreate.location

import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateLocationBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpCreateLocationFragment :
    BindingFragment<FragmentMeetUpCreateLocationBinding>(R.layout.fragment_meet_up_create_location) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    private var _locationAdapter: MeetUpCreateLocationAdapter? = null
    private val locationAdapter get() = requireNotNull(_locationAdapter)

    override fun initView() {
        viewModel.setEmptyLocationList()
        initRecyclerView()
        initObserveMeetUpLocationState()
        updateNextButton(false)
        setupLocationNameEditorAction()
    }

    private fun setupLocationNameEditorAction() {
        binding.etMeetUpCreateLocationNameEnter.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputText = binding.etMeetUpCreateLocationNameEnter.text.toString()
                viewModel.getMeetUpCreateLocation(inputText)
                handled = true
            }
            handled
        }
    }

    private fun initRecyclerView() {
        _locationAdapter = MeetUpCreateLocationAdapter(
            onMeetUpCreateLocationSelected = { selectedItem ->
                viewModel.setMeetUpLocation(selectedItem.location)
                updateNextButton(true)
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
