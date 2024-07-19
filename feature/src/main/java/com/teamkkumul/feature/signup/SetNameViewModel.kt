package com.teamkkumul.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.KeyStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetNameViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userInfoRepository: UserInfoRepository,
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
                        saveMemberName(name)
                    }
                }.onFailure {
                    _updateNameState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    private fun saveMemberName(input: String?) {
        viewModelScope.launch {
            if (input != null && input != KeyStorage.DATA_NULL) {
                userInfoRepository.saveMemberName(input)
            }
        }
    }
}
