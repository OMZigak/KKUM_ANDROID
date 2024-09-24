package com.teamkkumul.feature.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer<T> {
    private var debounceJob: Job? = null
    fun setDelay(value: T, delay: Long, action: (T) -> Unit) {
        debounceJob?.cancel()
        debounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(delay)
            action(value)
        }
    }
}
