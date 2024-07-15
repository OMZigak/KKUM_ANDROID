package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyInfoInputBinding
import com.teamkkumul.feature.meetup.readystatus.readyinfoinput.alarm.AlarmReceiver
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.TimeStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class ReadyInfoInputFragment :
    BindingFragment<FragmentReadyInfoInputBinding>(R.layout.fragment_ready_info_input) {
    private val viewModel: ReadyInfoInputViewModel by viewModels()

    private val setTimeDebouncer = Debouncer<String>()

    override fun initView() {
        initReadyInputBtnClick()
        setReadyHour()
        setReadyMinute()
        setMovingHour()
        setMovingMinute()

        viewModel.readyInputState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            binding.btnReadyInfoNext.isEnabled = state
        }.launchIn(viewLifeCycleScope)
    }

    private fun setReadyHour() {
        binding.etReadyStatusReadHour.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setReadyHour(delayedText)
                }
            }
        }

        viewModel.readyHour.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            binding.tilReadyStatusReadHour.boxStrokeColor = color
            binding.etReadyStatusReadHour.setTextColor(color)
            if (!isValid) {
                binding.etReadyStatusReadHour.setText(TimeStorage.END_HOUR.toString())
                toast(getString(R.string.ready_info_input_hour_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setReadyMinute() {
        binding.etReadyStatusReadyMinute.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setReadyMinute(delayedText)
                }
            }
        }
        viewModel.readyMinute.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            binding.tilReadyStatusReadyMinute.boxStrokeColor = color
            binding.etReadyStatusReadyMinute.setTextColor(color)
            if (!isValid && binding.etReadyStatusReadyMinute.text.toString() != "") {
                binding.etReadyStatusReadyMinute.setText(TimeStorage.END_MINUTE.toString())
                toast(getString(R.string.ready_info_input_minute_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setMovingHour() {
        binding.etReadyStatusMovingHour.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setMovingHour(delayedText)
                }
            }
        }
        viewModel.movingHour.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            binding.tilReadyStatusMovingHour.boxStrokeColor = color
            binding.etReadyStatusMovingHour.setTextColor(color)
            if (!isValid) {
                binding.etReadyStatusMovingHour.setText(TimeStorage.END_HOUR.toString())
                toast(getString(R.string.ready_info_input_hour_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setMovingMinute() {
        binding.etReadyStatusMovingMinute.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setMovingMinute(delayedText)
                }
            }
        }
        viewModel.movingMinute.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            binding.tilReadyStatusMovingMinute.boxStrokeColor = color
            binding.etReadyStatusMovingMinute.setTextColor(color)
            if (!isValid) {
                binding.etReadyStatusMovingMinute.setText(TimeStorage.END_MINUTE.toString())
                toast(getString(R.string.ready_info_input_minute_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initReadyInputBtnClick() {
        binding.btnReadyInfoNext.setOnClickListener {
            val readyHour = binding.etReadyStatusReadHour.text.toString().toInt()
            val readyMinute = binding.etReadyStatusReadyMinute.text.toString().toInt()
            val movingHour = binding.etReadyStatusMovingHour.text.toString().toInt()
            val movingMinute = binding.etReadyStatusMovingMinute.text.toString().toInt()

            // 준비 시작 알람 설정
            val readyTime = calculateFutureTime(readyHour, readyMinute)
            setAlarm(
                readyTime,
                getString(R.string.ready_info_input_ready_title),
                getString(R.string.ready_info_input_ready_content, "열기팟", "모각작"),
                0,
            )

            val movingTime = calculateFutureTime(movingHour, movingMinute)
            setAlarm(
                movingTime,
                getString(R.string.ready_info_input_ready_title),
                getString(R.string.ready_info_input_ready_content, "열기팟", "모각작"),
                1,
            )

            findNavController().navigate(R.id.fragment_home)
        }
    }

    private fun calculateFutureTime(hour: Int, minute: Int): Int {
        return hour * 60 + minute
    }

    private fun setAlarm(
        minutesFromNow: Int,
        alarmTitle: String,
        alarmContent: String,
        requestCode: Int,
    ) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, minutesFromNow)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra(KeyStorage.ALARM_TITLE, alarmTitle)
            putExtra(KeyStorage.ALARM_CONTENT, alarmContent)
            putExtra(KeyStorage.TAB_INDEX, 1)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Timber.tag("AlarmSet").d("$alarmTitle Alarm set for: ${calendar.time}")
    }
}
