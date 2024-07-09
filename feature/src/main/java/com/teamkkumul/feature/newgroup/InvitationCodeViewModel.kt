package com.teamkkumul.feature.newgroup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitationCodeViewModel @Inject constructor() : ViewModel() {
    private val _inputInvitationCode = MutableLiveData<String>()
}