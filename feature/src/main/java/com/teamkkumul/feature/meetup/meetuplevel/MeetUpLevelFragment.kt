package com.teamkkumul.feature.meetup.meetuplevel

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpLevelBinding

class MeetUpLevelFragment :
    BindingFragment<FragmentMeetUpLevelBinding>(R.layout.fragment_meet_up_level) {
    override fun initView() {
        val meetUpLevel = binding.cgMeetUpLevel
        val penalty = binding.cgSetPenalty
        val btnCreateMeetUp = binding.btnCreateMeetUp

        val chipGroups = listOf(meetUpLevel, penalty)

        chipGroups.forEach { chipGroup ->
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    updateChipStyle(chip, isChecked)
                    updateButtonState(chipGroups, btnCreateMeetUp)
                }
            }
        }
    }

    private fun updateChipStyle(chip: Chip, isChecked: Boolean) {
        if (isChecked) {
            chip.setChipBackgroundColorResource(R.color.green2)
            chip.setChipStrokeColorResource(R.color.green3)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.green3))
        } else {
            chip.setChipBackgroundColorResource(R.color.white0)
            chip.setChipStrokeColorResource(R.color.gray2)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray5))
        }
    }

    private fun updateButtonState(chipGroups: List<ChipGroup>, button: View) {
        val isAnyChipSelected = chipGroups.all { chipGroup ->
            (0 until chipGroup.childCount).any { (chipGroup.getChildAt(it) as Chip).isChecked }
        }
        button.isEnabled = isAnyChipSelected
    }
}
