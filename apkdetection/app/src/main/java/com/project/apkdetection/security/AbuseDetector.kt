package com.project.apkdetection.security

data class AbuseResult(
    val hasOverlay: Boolean,
    val hasSmsAccess: Boolean,
    val isOverlaySmsCombo: Boolean
)

object AbuseDetector {

    fun detect(permissions: Array<String>): AbuseResult {

        val hasOverlay = permissions.any {
            it.contains("SYSTEM_ALERT_WINDOW")
        }

        val hasSmsAccess = permissions.any {
            it.contains("READ_SMS") ||
                    it.contains("SEND_SMS") ||
                    it.contains("RECEIVE_SMS")
        }

        val combo = hasOverlay && hasSmsAccess

        return AbuseResult(
            hasOverlay = hasOverlay,
            hasSmsAccess = hasSmsAccess,
            isOverlaySmsCombo = combo
        )
    }
}
