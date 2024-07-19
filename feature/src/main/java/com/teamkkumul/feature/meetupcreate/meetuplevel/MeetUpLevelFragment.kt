package com.teamkkumul.feature.meetupcreate.meetuplevel

import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpLevelBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateViewModel
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.model.MeetUpCreateModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MeetUpLevelFragment :
    BindingFragment<FragmentMeetUpLevelBinding>(R.layout.fragment_meet_up_level) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    private var id = -1

    override fun initView() {
        val id = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1
        val selectedItems = arguments?.getIntArray("selectedItems")?.toList() ?: emptyList()

        Timber.d("Selected Items: $selectedItems")
        Timber.d("Meeting ID: $id")

        viewModel.setProgressBar(75)
        observeProgress()

        val meetUpLevel = binding.cgMeetUpLevel
        val penalty = binding.cgSetPenalty
        val btnCreateMeetUp = binding.btnCreateMeetUp

        val chipGroups = listOf(meetUpLevel, penalty)
        setupChipGroups(chipGroups, btnCreateMeetUp)
        setupCreateMeetUpButton(btnCreateMeetUp, id, selectedItems)
        initObserveMeetUpCreate()
    }

    private fun initObserveMeetUpCreate() {
        viewModel.meetUpCreateState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_level_to_fragment_add_meet_up_complete,
                        bundleOf(KeyStorage.PROMISE_ID to it.data),
                    )
                }

                is UiState.Failure -> Timber.tag("meetupcreate").d(it.errorMessage)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
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

    private fun setupCreateMeetUpButton(button: Button, id: Int, selectedItems: List<Int>) {
        button.setOnClickListener {
            val selectedMeetUpLevel = getSelectedChipText(binding.cgMeetUpLevel)
            val selectedPenalty = getSelectedChipText(binding.cgSetPenalty)

            Timber.d("Selected Meet Up Level~: $selectedMeetUpLevel")
            Timber.d("Selected Penalty~: $selectedPenalty")

            val bundle = arguments?.apply {
                putString(KeyStorage.MEET_UP_LEVEL, selectedMeetUpLevel)
                putString(KeyStorage.PENALTY, selectedPenalty)
            }

            val dressUpLevel = preprocessDressUpLevel(selectedMeetUpLevel)
            val time = convertToServerTimeFormat(
                arguments?.getString(KeyStorage.MEET_UP_DATE) ?: "",
            )

            val meetUpCreateModel = MeetUpCreateModel(
                name = arguments?.getString(KeyStorage.MEET_UP_NAME) ?: "",
                address = null,
                dressUpLevel = dressUpLevel,
                penalty = selectedPenalty,
                placeName = arguments?.getString(KeyStorage.MEET_UP_LOCATION) ?: "",
                roadAddress = null,
                time = "2024-07-07 15:00:00",
                participants = selectedItems,
                x = 0.0,
                y = 0.0,
            )

            viewModel.postMeetUpCreate(id, meetUpCreateModel)
        }
    }

    private fun convertToServerTimeFormat(dateTime: String): String {
        // Define the input date and time format
        val inputFormat = SimpleDateFormat("yyyy.MM.dda hh:mm", Locale.getDefault())
        // Define the output date and time format
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return try {
            // Parse the input date and time string
            val date = inputFormat.parse(dateTime) ?: return "Invalid date"
            // Format the parsed date into the desired output format
            outputFormat.format(date)
        } catch (e: ParseException) {
            // Handle the case where the input format does not match
            "Invalid date"
        }
    }

    private fun preprocessDressUpLevel(dressUpLevel: String): String {
        // Define a regular expression pattern to match "LV" followed by digits
        val pattern = Regex("LV\\s*(\\d+)")

        // Find the first match in the input string
        val matchResult = pattern.find(dressUpLevel)

        return if (matchResult != null) {
            // Extract the digit part and concatenate with "LV"
            "LV${matchResult.groupValues[1]}"
        } else {
            // Return a default value or an empty string if no match is found
            "LV1" // or return "" if you prefer
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
}
