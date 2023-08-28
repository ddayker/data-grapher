package com.dayker.datagrapher.data.storage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.core.content.FileProvider
import com.dayker.datagrapher.R
import java.io.File

class AndroidResourceManager(private val context: Context) : ResourceManager {
    override fun createImageFile(): File {
        val cachePath = File(context.cacheDir, "images").apply { mkdirs() }
        return File(cachePath, "image.jpg")
    }

    override fun getImageFileUri(imageFile: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.getString(R.string.file_provider_authority),
            imageFile
        )
    }

    /**
     * Grants necessary read and write permissions for the given URI to the selected apps in the chooser list.
     *
     * @param uri The URI for which permissions need to be granted.
     * @param chooser The intent chooser containing the list of apps to which permissions will be granted.
     */
    override fun grantUriPermissionsForChooser(uri: Uri, chooser: Intent) {
        val resInfoList: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }
}


