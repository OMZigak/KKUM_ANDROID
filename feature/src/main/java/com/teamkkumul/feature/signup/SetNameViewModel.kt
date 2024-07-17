package com.teamkkumul.feature.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetNameViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _updateNameState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val updateNameState get() = _updateNameState.asStateFlow()

    fun updateName(request: String) {
        viewModelScope.launch {
            profileRepository.updateName(request)
                .onSuccess { name ->
                    if (name.isNotEmpty()) {
                        _name.value = name
                        _updateNameState.emit(UiState.Success(name))
                        Log.e("UpdateName", "이름 업데이트 성공")
                    }
                }.onFailure {
                    _updateNameState.emit(UiState.Failure(it.message.toString()))
                    Log.e("UpdateName", "이름 업데이트 실패")
                }
        }
    }
}
