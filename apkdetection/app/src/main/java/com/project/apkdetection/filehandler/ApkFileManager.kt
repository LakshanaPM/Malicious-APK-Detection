package com.project.apkdetection.filehandler

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ApkFileManager {

    fun copyApkToCache(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val apkFile = File(context.cacheDir, "temp.apk")

            val outputStream = FileOutputStream(apkFile)
            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            apkFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
