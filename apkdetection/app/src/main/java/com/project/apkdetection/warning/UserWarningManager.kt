package com.project.apkdetection.warning

import android.content.Context
import androidx.appcompat.app.AlertDialog

object UserWarningManager {

    fun showWarning(context: Context, message: String, onProceed: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Security Warning")
            .setMessage(message)
            .setPositiveButton("Proceed") { _, _ -> onProceed() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
