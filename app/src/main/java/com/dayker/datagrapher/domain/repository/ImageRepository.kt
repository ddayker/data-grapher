package com.dayker.datagrapher.domain.repository

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    fun saveImage(bitmap: Bitmap, name: String): Boolean
    fun getImageUri(image: Bitmap): Uri
    fun grantUriPermissionsForChooser(uri: Uri, chooser: Intent)
    fun createChooserIntent(uri: Uri, title: String): Intent
}