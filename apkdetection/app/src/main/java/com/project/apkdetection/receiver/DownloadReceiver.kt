package com.project.apkdetection.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "APK Download Detected", Toast.LENGTH_SHORT).show()
    }
}
