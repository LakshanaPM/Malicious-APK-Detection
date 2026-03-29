package com.project.apkdetection.security

data class CertificateResult(
    val isSigned: Boolean,
    val sha256: String,
    val isDebug: Boolean,
    val reason: String
) {
    companion object {
        fun invalid(reason: String) = CertificateResult(
            isSigned = false,
            sha256 = "UNKNOWN",
            isDebug = false,
            reason = reason
        )
    }
}
