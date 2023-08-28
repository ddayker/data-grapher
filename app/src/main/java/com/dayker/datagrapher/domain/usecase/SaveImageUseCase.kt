package com.dayker.datagrapher.domain.usecase

import android.graphics.Bitmap
import com.dayker.datagrapher.domain.repository.ImageRepository

class SaveImageUseCase(private val imageRepository: ImageRepository) {

    fun execute(bitmap: Bitmap, name: String): Boolean {
        return imageRepository.saveImage(bitmap = bitmap, name = name)
    }
}