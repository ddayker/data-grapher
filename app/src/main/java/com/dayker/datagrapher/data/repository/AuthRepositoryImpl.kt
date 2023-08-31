package com.dayker.datagrapher.data.repository

import com.dayker.datagrapher.domain.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl() : AuthRepository {

    override suspend fun signUpByEmail(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInByEmail(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            Firebase.auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}






