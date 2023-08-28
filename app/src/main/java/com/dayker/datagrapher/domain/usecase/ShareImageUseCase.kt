package com.dayker.datagrapher.domain.usecase

import android.content.Intent
import android.graphics.Bitmap
import com.dayker.datagrapher.domain.repository.ImageRepository

class ShareImageUseCase(private val imageRepository: ImageRepository) {

    fun execute(bitmap: Bitmap, title: String): Intent {
        val uri = imageRepository.getImageUri(bitmap)
        val chooser = imageRepository.createChooserIntent(uri = uri, title = title)
        imageRepository.grantUriPermissionsForChooser(chooser = chooser, uri = uri)
        return chooser
    }
}