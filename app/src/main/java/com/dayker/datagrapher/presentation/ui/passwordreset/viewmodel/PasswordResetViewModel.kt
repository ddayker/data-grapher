package com.dayker.datagrapher.presentation.ui.passwordreset.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayker.datagrapher.domain.usecase.ResetPasswordUseCase
import com.dayker.datagrapher.presentation.core.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    ViewModel() {

    private val _resetResult = SingleLiveEvent<Result<Boolean>>()
    val resetResult: LiveData<Result<Boolean>> = _resetResult

    fun resetUserPassword(email: String) {
        viewModelScope.launch {
            val result = resetPasswordUseCase.execute(email)
            _resetResult.value = result
        }
    }

}