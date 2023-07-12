package com.puzzling.puzzlingaos.presentation.main.invitationCode

import android.util.Log
import androidx.lifecycle.*
import com.puzzling.puzzlingaos.data.model.response.ResponseInvitationCodeDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InvitationCodeViewModel : ViewModel() {

    val inputCode = MutableStateFlow("")
    val inputCodeLength = inputCode.map { inputCode ->
        inputCode.length.toString()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val inputNickName = MutableStateFlow("")
    val inputNickNameLength = inputNickName.map { inputNickName ->
        inputNickName.length.toString()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val inputRole = MutableStateFlow("")
    val inputRoleLength = inputRole.map { inputRole ->
        inputRole.length.toString()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    private val _codeResponse = MutableLiveData<ResponseInvitationCodeDto.InvitationCodeData?>(null)
    val codeResponse: LiveData<ResponseInvitationCodeDto.InvitationCodeData?> get() = _codeResponse

    private val _isCodeSucces = MutableLiveData<Boolean>()
    val isCodeSuccess: LiveData<Boolean> get() = _isCodeSucces

    private val _isProfileSucces = MutableLiveData<Boolean>()
    val isProfileSuccess: LiveData<Boolean> get() = _isProfileSucces

    fun isCodeValid() = viewModelScope.launch {
        // 서버 통신 추가 예정
        _codeResponse.value = ResponseInvitationCodeDto.InvitationCodeData(4, "pickle")
        _isCodeSucces.value = true
    }

    fun joinProject() = viewModelScope.launch {
        // 서버 통신 추가 예정
        _isProfileSucces.value = true
        Log.d("프로젝트 참여하기", "프로젝트 참여하기 ${_codeResponse.value?.projectName}")
    }
}
