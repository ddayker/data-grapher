package com.dayker.datagrapher.data.storage

import android.content.Intent
import android.net.Uri
import java.io.File

interface ResourceManager {

    fun createImageFile(): File
    fun getImageFileUri(imageFile: File): Uri
    fun grantUriPermissionsForChooser(uri: Uri, chooser: Intent)
}