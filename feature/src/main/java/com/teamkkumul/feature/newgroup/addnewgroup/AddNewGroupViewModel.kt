package com.teamkkumul.feature.newgroup.addnewgroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddNewGroupViewModel : ViewModel() {

    private val meetingsService = MeetingsService
    private val _meetings = MutableLiveData<ResponseAddNewGroupDto>()
    val meetings: LiveData<ResponseAddNewGroupDto> = _meetings
    fun addNewGroup(request: RequestAddNewGroupDto) {
        viewModelScope.launch {
            runCatching {
                meetingsService.addNewGroup(request)
            }.onSuccess {
                Log.e("addNewGroup", "모임 생성 성공")
            }.onFailure {
                if (it is HttpException) {
                    Log.e("addNewGroup", "모임 생성 실패 - http exception")
                } else {
                    Log.e("addNewGroup", "모임 생성 실패")
                }
            }
        }
    }
}
