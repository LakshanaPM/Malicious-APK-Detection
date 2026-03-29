package com.project.apkdetection.model

data class ApkInfo(
    val appName: String,
    val packageName: String,
    val permissions: Array<String>
)
