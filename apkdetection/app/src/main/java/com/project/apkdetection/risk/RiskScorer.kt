package com.project.apkdetection.risk

import com.project.apkdetection.security.AbuseResult
import com.project.apkdetection.security.CertificateResult

class RiskScorer {

    fun calculate(
        totalPermissions: Int,
        dangerousPermissions: Int,
        certResult: CertificateResult,
        abuseResult: AbuseResult
    ): Int {

        var score = 0

        score += totalPermissions * 5
        score += dangerousPermissions * 20

        if (!certResult.isSigned) score += 40
        if (certResult.isDebug) score += 30

        if (abuseResult.hasOverlay) score += 25
        if (abuseResult.hasSmsAccess) score += 25
        if (abuseResult.isOverlaySmsCombo) score += 40 // VERY dangerous

        return score
    }
}
