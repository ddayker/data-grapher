package com.dayker.datagrapher.presentation.ui.authorization.viewmodel

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayker.datagrapher.domain.repository.GoogleAuthRepository
import com.dayker.datagrapher.domain.usecase.SignInByEmailUseCase
import com.dayker.datagrapher.presentation.core.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val signInByEmailUseCase: SignInByEmailUseCase,
    private val googleAuthRepository: GoogleAuthRepository
) : ViewModel() {

    private val _authorizationResult = SingleLiveEvent<Result<Boolean>>()
    val authorizationResult: LiveData<Result<Boolean>> = _authorizationResult

    fun authorizeUserByEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = signInByEmailUseCase.execute(email, password)
            _authorizationResult.value = result
        }
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