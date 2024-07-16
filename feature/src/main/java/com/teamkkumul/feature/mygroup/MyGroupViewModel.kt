package com.teamkkumul.feature.mygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor() : ViewModel() {

    private val _group: MutableLiveData<UiState<List<MyGroupModel.Meeting>>> =
        MutableLiveData(UiState.Loading)
    val group: LiveData<UiState<List<MyGroupModel.Meeting>>> get() = _group
    fun getPromise() {
        viewModelScope.launch {
            _group.value = UiState.Empty
        }
    }

    init {
        val mockGroups = listOf(
            MyGroupModel.Meeting(id = 3, 3, "모각작"),
        )
        _group.value = UiState.Success(mockGroups)
    }
}
