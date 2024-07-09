package com.teamkkumul.feature.newgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupNameViewModel @Inject constructor() : ViewModel() {
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> get() = _groupName

    fun getGroupName(text: String) {
        _groupName.value = text
    }
}
