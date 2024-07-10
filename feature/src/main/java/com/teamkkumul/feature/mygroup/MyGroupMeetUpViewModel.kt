package com.teamkkumul.feature.mygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupMeetUpModel
import kotlinx.coroutines.launch

class MyGroupMeetUpViewModel : ViewModel() {
    private val _promise: MutableLiveData<UiState<List<MyGroupMeetUpModel.Promise>>> =
        MutableLiveData(UiState.Loading)
    val promise: LiveData<UiState<List<MyGroupMeetUpModel.Promise>>> get() = _promise

    fun getPromise() {
        viewModelScope.launch {
            _promise.value = UiState.Empty
        }
    }

    init {
        getPromise()
    }
}
