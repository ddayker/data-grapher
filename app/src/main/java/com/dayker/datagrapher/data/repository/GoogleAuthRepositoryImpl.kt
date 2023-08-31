package com.dayker.datagrapher.data.repository

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.dayker.datagrapher.domain.repository.GoogleAuthRepository
import com.dayker.datagrapher.utils.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GoogleAuthRepositoryImpl(private val oneTapClient: SignInClient) : GoogleAuthRepository {

    private val auth = Firebase.auth

    private fun buildSignInRequest(webClientId: String): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override fun startGoogleSignIn(
        webClientId: String,
        startIntentSender: (intentSender: IntentSender) -> Unit,
        onFailureAction: () -> Unit
    ) {
        oneTapClient.beginSignIn(buildSignInRequest(webClientId))
            .addOnSuccessListener { result ->
                try {
                    val intentSender = result.pendingIntent.intentSender
                    startIntentSender(intentSender)
                } catch (e: IntentSender.SendIntentException) {
                    onFailureAction()
                    e.localizedMessage?.let { Log.e(javaClass.name, it) }
                }
            }
            .addOnFailureListener { e ->
                onFailureAction()
                e.localizedMessage?.let { Log.e(javaClass.name, it) }
            }
    }


    override fun handleGoogleSignInResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onSuccessAction: () -> Unit,
        onFailureAction: () -> Unit,
    ) {
        if (requestCode == Constants.REQ_ONE_TAP) {
            try {
                val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = googleCredential.googleIdToken
                when {
                    idToken != null -> {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onSuccessAction()
                                } else {
                                    onFailureAction()
                                    task.exception?.message?.let { Log.e(javaClass.name, it) }
                                }
                            }
                    }
                    else -> {
                        onFailureAction()
                        Log.e(javaClass.name, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                onFailureAction()
                e.message?.let { Log.e(javaClass.name, it) }
            }
        }
    }

}