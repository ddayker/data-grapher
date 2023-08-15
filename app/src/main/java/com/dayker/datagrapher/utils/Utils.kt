package com.dayker.datagrapher.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.SeekBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dayker.datagrapher.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

object Utils {

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }

    fun isBold(valueTypeface: Typeface) = valueTypeface == Typeface.create(
        Typeface.DEFAULT,
        Typeface.BOLD
    ) || valueTypeface == Typeface.create(
        Typeface.DEFAULT,
        Typeface.BOLD_ITALIC
    )

    fun isItalic(valueTypeface: Typeface) = valueTypeface == Typeface.create(
        Typeface.DEFAULT,
        Typeface.ITALIC
    ) || valueTypeface == Typeface.create(
        Typeface.DEFAULT,
        Typeface.BOLD_ITALIC
    )

    fun showColorPickerDialog(context: Context, title: String, viewModelAction: (String) -> Unit) {
        ColorPickerDialogBuilder
            .with(context)
            .setTitle(title)
            .initialColor(Color.WHITE)
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(12)
            .setPositiveButton(R.string.Ok) { _, selectedColor, _ ->
                viewModelAction(Integer.toHexString(selectedColor))
            }
            .setNegativeButton(R.string.Cancel) { _, _ -> }
            .build()
            .show()
    }

    fun showEditTextDialog(context: Context, title: String, action: (String) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextCenterText)
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(R.string.Ok) { _, _ ->
                val enteredText = editText.text.toString()
                action(enteredText)
            }
            .setNegativeButton(R.string.Cancel, null)
            .create()
        dialog.show()
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

    fun updateTypeface(
        isBold: Boolean,
        isItalic: Boolean,
        updateFunction: (Typeface) -> Unit
    ) {
        val typefaceStyle = when {
            isBold && isItalic -> Typeface.BOLD_ITALIC
            isBold -> Typeface.BOLD
            isItalic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
        val typeface = Typeface.create(Typeface.DEFAULT, typefaceStyle)
        updateFunction(typeface)
    }


}