package com.project.apkdetection.security

object PermissionAnalyzer {

    private val dangerousPermissions = setOf(
        "READ_SMS",
        "RECORD_AUDIO",
        "READ_CONTACTS",
        "ACCESS_FINE_LOCATION",
        "READ_CALL_LOG"
    )

    fun findDangerousPermissions(perms: Array<String>): List<String> {
        return perms.filter { perm ->
            dangerousPermissions.any { perm.contains(it) }
        }
    }
}
