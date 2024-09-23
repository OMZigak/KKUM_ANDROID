package com.teamkkumul.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _updateImageState = MutableSharedFlow<UiState<Unit>>()
    val updateImageState get() = _updateImageState.asSharedFlow()

    private var _photoUri: String? = null
    val photoUri: String? get() = _photoUri

    fun setPhotoUri(uri: String?) {
        _photoUri = uri
    }

    fun updateImage(imageString: String?) {
        viewModelScope.launch {
            _updateImageState.emit(UiState.Loading)
            profileRepository.updateImage(imageString)
                .onSuccess {
                    _updateImageState.emit(UiState.Success(Unit))
                }.onFailure {
                    _updateImageState.emit(UiState.Failure(it.message.orEmpty()))
                }
        }
    }

    fun deleteImage() {
        viewModelScope.launch {
            profileRepository.deleteImage()
                .onSuccess {
                    _updateImageState.emit(UiState.Success(Unit))
                }.onFailure {
                    _updateImageState.emit(UiState.Failure(it.message.orEmpty()))
                }
        }
    }
}
