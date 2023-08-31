package com.dayker.datagrapher.presentation.ui.registration.viewmodel

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayker.datagrapher.domain.repository.GoogleAuthRepository
import com.dayker.datagrapher.domain.usecase.SignUpByEmailUseCase
import com.dayker.datagrapher.presentation.core.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val signUpByEmailUseCase: SignUpByEmailUseCase,
    private val googleAuthRepository: GoogleAuthRepository
) :
    ViewModel() {

    private val _registrationResult = SingleLiveEvent<Result<Boolean>>()
    val registrationResult: LiveData<Result<Boolean>> = _registrationResult

    fun registerUserByEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = signUpByEmailUseCase.execute(email, password)
            _registrationResult.value = result
        }
    }

    fun validPasswordByLength(password: String, errorMessage: String): String? {
        return if (password.length < 6) {
            errorMessage
        } else null
    }

    fun validPasswordConfirmation(
        password: String,
        confirmation: String,
        errorMessage: String
    ): String? {
        return if (password != confirmation
        ) {
            errorMessage
        } else null
    }

    fun startGoogleSignIn(
        webClientId: String,
        onSuccess: (intentSender: IntentSender) -> Unit,
        onFailureAction: () -> Unit
    ) {
        googleAuthRepository.startGoogleSignIn(
            webClientId = webClientId,
            startIntentSender = onSuccess,
            onFailureAction = onFailureAction
        )
    }

    fun handleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onSuccessAction: () -> Unit,
        onFailureAction: () -> Unit
    ) {
        googleAuthRepository.handleGoogleSignInResult(
            requestCode,
            resultCode,
            data,
            onSuccessAction,
            onFailureAction
        )
    }

}