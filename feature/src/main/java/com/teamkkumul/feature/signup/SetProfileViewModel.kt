package com.teamkkumul.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _updateImageResult = MutableLiveData<Result<Unit>>()
    val updateImageResult: LiveData<Result<Unit>> get() = _updateImageResult

    fun updateImage(file: File) {
        viewModelScope.launch {
            val result = profileRepository.updateImage(file)
            _updateImageResult.value = result
        }
    }
}
