package com.example.welfarebenefits.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog

interface ShowAlertDialogListener {
    fun onPositiveButtonClicked() {}
    fun onNegativeButtonClicked() {}
}

class ShowAlertDialog(val context: Context, private val title: String, private val message: String,
                      private val positiveButtonLabel: String? = null, private val negativeButtonLabel: String? = null,
                      private val listener: ShowAlertDialogListener? = null) {

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonLabel) { dialog, _ ->
                listener?.onPositiveButtonClicked()
            }
            .setNegativeButton(negativeButtonLabel) { dialog, _ ->
                listener?.onNegativeButtonClicked()
            }
            .show()
    }
}