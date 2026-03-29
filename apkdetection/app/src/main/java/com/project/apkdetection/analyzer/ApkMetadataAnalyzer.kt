package com.project.apkdetection.analyzer

import android.content.Context
import android.content.pm.PackageManager
import com.project.apkdetection.model.ApkInfo
import java.io.File

object ApkMetadataAnalyzer {

    fun analyze(context: Context, apkFile: File): ApkInfo {

        val pm = context.packageManager

        val packageInfo = pm.getPackageArchiveInfo(
            apkFile.absolutePath,
            PackageManager.GET_PERMISSIONS
        ) ?: return ApkInfo(
            appName = "Unknown",
            packageName = "Unknown",
            permissions = emptyArray()
        )

        // 🔑 SAFELY unwrap ApplicationInfo
        val appInfo = packageInfo.applicationInfo
            ?: return ApkInfo(
                appName = "Unknown",
                packageName = packageInfo.packageName ?: "Unknown",
                permissions = emptyArray()
            )

        // 🔑 REQUIRED for external APK parsing
        appInfo.sourceDir = apkFile.absolutePath
        appInfo.publicSourceDir = apkFile.absolutePath

        val appName = pm.getApplicationLabel(appInfo).toString()

        return ApkInfo(
            appName = appName,
            packageName = packageInfo.packageName ?: "Unknown",
            permissions = packageInfo.requestedPermissions ?: emptyArray()
        )
    }
}
