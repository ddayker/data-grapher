package com.dayker.datagrapher.domain.usecase

import com.dayker.datagrapher.domain.repository.AuthRepository

class SignUpByEmailUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password: String): Result<Boolean> {
        return authRepository.signUpByEmail(email = email, password = password)
    }
}