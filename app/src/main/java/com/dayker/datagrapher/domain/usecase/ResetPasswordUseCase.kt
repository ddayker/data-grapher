package com.dayker.datagrapher.domain.usecase

import com.dayker.datagrapher.domain.repository.AuthRepository

class ResetPasswordUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String): Result<Boolean> {
        return authRepository.resetPassword(email = email)
    }
}