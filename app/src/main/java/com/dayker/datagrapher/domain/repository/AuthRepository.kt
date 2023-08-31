package com.dayker.datagrapher.domain.repository

interface AuthRepository {

    suspend fun signUpByEmail(email: String, password: String): Result<Boolean>
    suspend fun signInByEmail(email: String, password: String): Result<Boolean>
    suspend fun resetPassword(email: String): Result<Boolean>
}