package com.teamkkumul.feature.meetupcreate.meetuplevel

import android.view.View
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpLevelBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateViewModel
import com.teamkkumul.feature.utils.animateProgressBar

class MeetUpLevelFragment :
    BindingFragment<FragmentMeetUpLevelBinding>(R.layout.fragment_meet_up_level) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    override fun initView() {
        viewModel.setProgressBar(75)
        observeProgress()

        val meetUpLevel = binding.cgMeetUpLevel
        val penalty = binding.cgSetPenalty
        val btnCreateMeetUp = binding.btnCreateMeetUp

        val chipGroups = listOf(meetUpLevel, penalty)
        setupChipGroups(chipGroups, btnCreateMeetUp)
        setupCreateMeetUpButton(btnCreateMeetUp)
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpLevel
        viewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
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

    private fun setupCreateMeetUpButton(button: Button) {
        button.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_level_to_fragment_add_meet_up_complete)
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
}
