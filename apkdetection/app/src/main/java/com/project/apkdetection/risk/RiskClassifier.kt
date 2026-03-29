package com.project.apkdetection.risk

object RiskClassifier {

    fun classify(score: Int): RiskLevel {
        return when {
            score >= 80 -> RiskLevel.HIGH
            score >= 40 -> RiskLevel.SUSPICIOUS
            else -> RiskLevel.SAFE
        }
    }
}
