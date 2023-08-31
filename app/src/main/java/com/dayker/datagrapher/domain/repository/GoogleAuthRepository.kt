package com.dayker.datagrapher.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest

interface GoogleAuthRepository {

    fun handleGoogleSignInResult(requestCode: Int, resultCode: Int, data: Intent?, onSuccessAction :() -> Unit, onFailureAction: () -> Unit)
    fun startGoogleSignIn(webClientId: String, startIntentSender: (intentSender : IntentSender) -> Unit, onFailureAction: () -> Unit)
}