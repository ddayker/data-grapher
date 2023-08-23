package com.dayker.datagrapher.domain.usecase

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.OutputStream

class SaveChartAsImageUseCase(private val resolver: ContentResolver) {

    fun execute(bitmap: Bitmap, name: String): Boolean {
        val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }
        val imageUri = resolver.insert(imageCollection, contentValues)
        imageUri?.let { uri ->
            try {
                val outputStream: OutputStream? = resolver.openOutputStream(uri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    it.flush()
                    println("SAVED")
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        return false
    }
}