package com.teamkkumul.feature.signup

import androidx.lifecycle.ViewModel
import com.teamkkumul.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

}
