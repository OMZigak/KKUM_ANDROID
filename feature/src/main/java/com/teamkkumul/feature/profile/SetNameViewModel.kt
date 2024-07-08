package com.teamkkumul.feature.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetNameViewModel @Inject constructor() : ViewModel() {
    private val _inputName = MutableLiveData<String>()
    val inputName: LiveData<String> get() = _inputName

    fun getInputName(text: String) {
        _inputName.value = text
    }
}
