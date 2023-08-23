package com.dayker.datagrapher.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.dayker.datagrapher.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

object UIUtilities {

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

    fun showEditTextDialog(
        context: Context,
        title: String,
        hint: String,
        text: String = "",
        action: (String) -> Unit
    ): Boolean {
        var isPositiveButtonClicked = false
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextCenterText)
        val editTextField = dialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
        editText.setText(text)
        editTextField.hint = hint
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(R.string.Ok) { _, _ ->
                val enteredText = editText.text.toString()
                action(enteredText)
                isPositiveButtonClicked = true
            }
            .setNegativeButton(R.string.Cancel, null)
            .create()
        dialog.show()
        return isPositiveButtonClicked
    }

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        action: () -> Unit
    ) {
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(R.string.Ok) { dialog, which ->
            action()
        }
        alertDialogBuilder.setNegativeButton(R.string.Cancel) { dialog, which -> }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showSnackBar(message: String, view: View) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }

    /**
     * Displays a confirmation dialog for saving an image with the specified name.
     *
     * This function presents a dialog to the user, allowing them to enter a name for the image they want to save.
     * The user's input is then passed to the provided `saveAction` lambda function, which should handle the actual
     * saving process. After attempting to save the image, a Snackbar message is shown to inform the user about the
     * outcome of the save operation.
     */
    fun showSaveConfirmationDialog(
        context: Context,
        snackBarView: View,
        name: String,
        saveAction: (name: String) -> Boolean
    ) {
        showEditTextDialog(
            context = context,
            title = context.getString(R.string.save_as_image),
            hint = context.getString(R.string.image_name),
            text = name
        ) {
            val isSaved = saveAction(it)
            if (isSaved) {
                showSnackBar(
                    message = context.getString(R.string.image_saved),
                    view = snackBarView
                )
            } else {
                showSnackBar(
                    message = context.getString(R.string.image_saving_error),
                    view = snackBarView
                )
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
}