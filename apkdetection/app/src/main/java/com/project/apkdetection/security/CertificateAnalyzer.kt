package com.project.apkdetection.security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File
import java.security.MessageDigest

object CertificateAnalyzer {

    fun analyze(context: Context, apkFile: File): CertificateResult {

        val pm = context.packageManager

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            @Suppress("DEPRECATION")
            PackageManager.GET_SIGNATURES
        }

        val packageInfo = pm.getPackageArchiveInfo(apkFile.absolutePath, flags)
            ?: return CertificateResult.invalid("Package info not found")

        // 🔑 SAFELY unwrap ApplicationInfo
        val appInfo = packageInfo.applicationInfo
            ?: return CertificateResult.invalid("Application info missing")

        // 🔑 REQUIRED for external APK parsing
        appInfo.sourceDir = apkFile.absolutePath
        appInfo.publicSourceDir = apkFile.absolutePath

        val signatures = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }
        } catch (e: Exception) {
            null
        }

        if (signatures.isNullOrEmpty()) {
            return CertificateResult.invalid("No signatures found")
        }

        val certBytes = signatures[0].toByteArray()
        val sha256 = sha256(certBytes)

        val isDebugCert = isDebugCertificate(sha256)

        return CertificateResult(
            isSigned = true,
            sha256 = sha256,
            isDebug = isDebugCert,
            reason = if (isDebugCert) "Debug/Test certificate detected" else "Signed certificate"
        )
    }

    private fun sha256(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(bytes).joinToString(":") {
            "%02X".format(it)
        }
    }

    private fun isDebugCertificate(sha256: String): Boolean {
        // Known Android debug cert prefix (heuristic)
        return sha256.startsWith("F0:FD", ignoreCase = true)
    }
}
