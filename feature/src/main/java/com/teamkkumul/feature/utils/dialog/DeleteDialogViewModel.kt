package com.teamkkumul.feature.utils.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.AuthRepository
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
    private val myGroupRepository: MyGroupRepository,
    private val authRepository: AuthRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _deleteMyGroupState = MutableSharedFlow<UiState<Unit>>()
    val deleteMyGroupState get() = _deleteMyGroupState.asSharedFlow()

    private val _leaveMeetUpState = MutableSharedFlow<UiState<Unit>>()
    val leaveMeetUpState get() = _leaveMeetUpState.asSharedFlow()

    private val _deleteMeetUpState = MutableSharedFlow<UiState<Unit>>()
    val deleteMeetUpState get() = _deleteMeetUpState.asSharedFlow()

    private val _logoutState = MutableSharedFlow<UiState<Unit>>()
    val logoutState: SharedFlow<UiState<Unit>> = _logoutState.asSharedFlow()

    private val _withdrawState = MutableSharedFlow<UiState<Unit>>()
    val withdrawState: SharedFlow<UiState<Unit>> = _withdrawState.asSharedFlow()

    fun deleteMyGroup(meetingId: Int) {
        viewModelScope.launch {
            myGroupRepository.deleteMyGroup(meetingId).onSuccess {
                _deleteMyGroupState.emit(UiState.Success(Unit))
            }
                .onFailure { exception ->
                    _deleteMyGroupState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }

    fun leaveMeetUp(promiseId: Int) {
        viewModelScope.launch {
            meetUpRepository.leaveMeetUp(promiseId).onSuccess {
                _leaveMeetUpState.emit(UiState.Success(Unit))
            }
                .onFailure { exception ->
                    _leaveMeetUpState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }

    fun deleteMeetUp(promiseId: Int) {
        viewModelScope.launch {
            meetUpRepository.deleteMeetUp(promiseId).onSuccess {
                _deleteMeetUpState.emit(UiState.Success(Unit))
            }
                .onFailure { exception ->
                    _deleteMeetUpState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }

    fun postLogout() {
        viewModelScope.launch {
            authRepository.postLogout()
                .onSuccess {
                    _logoutState.emit(UiState.Success(Unit))
                    userInfoRepository.clearAll()
                }.onFailure {
                    _logoutState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun deleteWithdrawal() {
        viewModelScope.launch {
            authRepository.deleteWithdrawal()
                .onSuccess {
                    _withdrawState.emit(UiState.Success(Unit))
                    userInfoRepository.clearAll()
                }.onFailure {
                    _withdrawState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }
}
