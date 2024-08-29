package com.teamkkumul.feature.meetupcreate.meetuplevel

import android.view.View
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpLevelBinding
import com.teamkkumul.feature.meetupcreate.MeetUpLevelViewModel
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import com.teamkkumul.feature.utils.animateProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpLevelFragment :
    BindingFragment<FragmentMeetUpLevelBinding>(R.layout.fragment_meet_up_level) {
    private val sharedViewModel: MeetUpSharedViewModel by activityViewModels<MeetUpSharedViewModel>()
    private val viewModel: MeetUpLevelViewModel by viewModels<MeetUpLevelViewModel>()
    private var selectedItems: List<Int> = emptyList()

    override fun initView() {
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE

        sharedViewModel.setProgressBar(75)
        observeProgress()

        val meetUpLevel = binding.cgMeetUpLevel
        val penalty = binding.cgSetPenalty
        val btnCreateMeetUp = binding.btnCreateMeetUp

        val chipGroups = listOf(meetUpLevel, penalty)
        setupChipGroups(chipGroups, btnCreateMeetUp)

        if (sharedViewModel.isEditMode()) {
            setupEditMeetUpButton(btnCreateMeetUp, sharedViewModel.getPromiseId(), selectedItems)
            initObserveMeetUpEdit()
        } else {
            setupCreateMeetUpButton(btnCreateMeetUp, sharedViewModel.getMeetingId(), selectedItems)
            Timber.tag("meetingid").d(sharedViewModel.getMeetingId().toString())
            initObserveMeetUpCreate()
        }
    }

    private fun initObserveMeetUpCreate() {
        viewModel.meetUpCreateState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    sharedViewModel.updateMeetUpModel(promiseId = it.data)
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_level_to_fragment_add_meet_up_complete,
                    )
                }

                is UiState.Failure -> toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMeetUpEdit() {
        viewModel.meetUpEditState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
//                    viewModel.updateMeetUpModel(promiseId = it.data)
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_level_to_fragment_add_meet_up_complete,
                    )
                }

                is UiState.Failure -> toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpLevel
        sharedViewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            animateProgressBar(progressBar, 50, progress)
        }
    }

    private fun setupChipGroups(chipGroups: List<ChipGroup>, btnCreateMeetUp: Button) {
        chipGroups.forEach { chipGroup ->
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        val chipIndex = chipGroup.indexOfChild(buttonView)
                        onChipSelected(chipGroup, chipIndex)
                    }
                    updateChipStyle(chip, isChecked)
                    updateButtonState(chipGroups, btnCreateMeetUp)
                }
            }
        }
    }

    private fun setupCreateMeetUpButton(button: Button, id: Int, selectedItems: List<Int>) {
        button.setOnClickListener {
            val selectedMeetUpLevel = getSelectedChipText(binding.cgMeetUpLevel)
            val selectedPenalty = getSelectedChipText(binding.cgSetPenalty)

            Timber.d("Selected Meet Up Level~: $selectedMeetUpLevel")
            Timber.d("Selected Penalty~: $selectedPenalty")

            val dressUpLevel = preprocessDressUpLevel(selectedMeetUpLevel)
            val time =
                sharedViewModel.meetUpCreateModel.value.date + " " + sharedViewModel.meetUpCreateModel.value.time
            sharedViewModel.updateMeetUpModel(
                dressUpLevel = dressUpLevel,
                penalty = selectedPenalty,
                time = null,
            )
            sharedViewModel.updateMeetUpModel(time = time)
            Timber.tag("final").d(sharedViewModel.meetUpCreateModel.value.toString())

            viewModel.postMeetUpCreate(id, sharedViewModel.meetUpCreateModel.value)
        }
    }

    private fun setupEditMeetUpButton(button: Button, id: Int, selectedItems: List<Int>) {
        button.setOnClickListener {
            val selectedMeetUpLevel = getSelectedChipText(binding.cgMeetUpLevel)
            val selectedPenalty = getSelectedChipText(binding.cgSetPenalty)
            val dressUpLevel = preprocessDressUpLevel(selectedMeetUpLevel)
            sharedViewModel.updateMeetUpModel(
                dressUpLevel = dressUpLevel,
                penalty = selectedPenalty,
                time = sharedViewModel.meetUpCreateModel.value.date + " " + sharedViewModel.meetUpCreateModel.value.time,
            )
            Timber.tag("final edit").d(sharedViewModel.meetUpCreateModel.value.toString())

            viewModel.putMeetUpEdit(id, sharedViewModel.meetUpCreateModel.value)
        }
    }

    private fun preprocessDressUpLevel(dressUpLevel: String): String {
        val matchResult = DRESS_UP_LEVEL_PATTERN.find(dressUpLevel)

        return if (matchResult != null) {
            "LV${matchResult.groupValues[1]}"
        } else {
            "FREE"
        }
    }

    private fun onChipSelected(
        chipGroup: ChipGroup,
        chipIndex: Int,
    ) { // chipGroupIndex는 0 or 1, chipIndex는 0 ~ 4(5)
        val chipGroupIndex = when (chipGroup.id) {
            R.id.cg_meet_up_level -> 0
            R.id.cg_set_penalty -> 1
            else -> -1
        }
    }

    private fun updateChipStyle(chip: Chip, isChecked: Boolean) {
        val context = requireContext()
        if (isChecked) {
            chip.setChipBackgroundColorResource(R.color.green2)
            chip.setChipStrokeColorResource(R.color.green3)
            chip.setTextColor(context.colorOf(R.color.green3))
        } else {
            chip.setChipBackgroundColorResource(R.color.white0)
            chip.setChipStrokeColorResource(R.color.gray2)
            chip.setTextColor(context.colorOf(R.color.gray5))
        }
    }

    private fun updateButtonState(chipGroups: List<ChipGroup>, button: View) {
        val isAnyChipSelected = chipGroups.all { chipGroup ->
            (0 until chipGroup.childCount).any { (chipGroup.getChildAt(it) as Chip).isChecked }
        }
        button.isEnabled = isAnyChipSelected
    }

    private fun getSelectedChipText(chipGroup: ChipGroup): String {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                return chip.text.toString()
            }
        }
        return ""
    }

    companion object {
        private val DRESS_UP_LEVEL_PATTERN = Regex("LV\\s*(\\d+)")
    }
}
