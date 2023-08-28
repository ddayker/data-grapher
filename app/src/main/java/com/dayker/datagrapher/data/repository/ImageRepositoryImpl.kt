package com.dayker.datagrapher.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.dayker.datagrapher.data.storage.ResourceManager
import com.dayker.datagrapher.domain.repository.ImageRepository
import java.io.FileOutputStream
import java.io.OutputStream

class ImageRepositoryImpl(
    private val resourceManager: ResourceManager,
    private val resolver: ContentResolver
) : ImageRepository {
    override fun saveImage(bitmap: Bitmap, name: String): Boolean {
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
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        return false
    }

    override fun getImageUri(image: Bitmap): Uri {
        val imageFile = resourceManager.createImageFile()
        FileOutputStream(imageFile).use { outputStream ->
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return resourceManager.getImageFileUri(imageFile = imageFile)
    }

    override fun grantUriPermissionsForChooser(uri: Uri, chooser: Intent) {
        resourceManager.grantUriPermissionsForChooser(uri = uri, chooser = chooser)
    }

    override fun createChooserIntent(uri: Uri, title: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        return Intent.createChooser(intent, title)
    }
}