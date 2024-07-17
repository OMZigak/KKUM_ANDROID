package com.teamkkumul.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _updateImageState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val updateImageState get() = _updateImageState.asStateFlow()

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    fun setPhotoUri(uri: String?) {
        _photoUri.value = uri
    }

    fun updateImage(contentText: String, imageString: String?) {
        viewModelScope.launch {
            profileRepository.updateImage(contentText, imageString)
                .onSuccess { isSuccess ->
                    if (isSuccess) {
                        _updateImageState.emit(UiState.Success(Unit))
                    } else {
                        _updateImageState.emit(UiState.Failure("이미지 업로드 실패"))
                    }
                }.onFailure {
                    _updateImageState.emit(UiState.Failure(it.message.orEmpty()))
                }
        }
    }
}
