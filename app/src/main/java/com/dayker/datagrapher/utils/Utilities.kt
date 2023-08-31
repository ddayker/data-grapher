package com.dayker.datagrapher.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Patterns
import android.view.View
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dayker.datagrapher.R

object Utilities {

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }

    fun SeekBar.onProgressChanged(action: (progress: Int, fromUser: Boolean) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                action(progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun setupSeekBar(
        seekBar: SeekBar?,
        minProgress: Int = 0,
        changeFunction: (Float) -> Unit
    ) {
        seekBar?.onProgressChanged { progress, _ ->
            if (progress < minProgress) {
                seekBar.progress = minProgress
            } else {
                val value = progress.toFloat()
                changeFunction(value)
            }
        }
    }

    fun calculatePercentage(value: Float, generalValue: Float): String {
        return String.format("%.1f", value / generalValue * 100) + "%"
    }

    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun executeIfExternalStoragePermissionGranted(
        context: Context,
        activity: Activity,
        action: () -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constants.WRITE_EXTERNAL_STORAGE_REQUEST
            )
        } else {
            action()
        }
    }

    fun generateImageName(context: Context) =
        context.getString(R.string.image_name_prefix) + System.currentTimeMillis() + context.getString(
            R.string.image_name_postfix
        )


    fun validEmailHelper(email: String, context: Context): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            context.getString(R.string.invalid_email)
        } else null
    }
}