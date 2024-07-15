package com.teamkkumul.feature.meetupcreate

import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetUpCreateLocationFragment :
    BindingFragment<FragmentMeetUpCreateLocationBinding>(R.layout.fragment_meet_up_create_location) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    private var _locationAdapter: MeetUpCreateLocationAdapter? = null
    private val locationAdapter get() = requireNotNull(_locationAdapter)

    override fun initView() {
        initRecyclerView()
        initObserveViewModel()
        updateNextButton(false)
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

    private fun initObserveViewModel() {
        viewModel.location.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Empty -> {
                }

                is UiState.Success -> {
                    locationAdapter.submitList(it.data)
                }

                else -> {}
            }
        }
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
