package com.project.apkdetection.model

import com.project.apkdetection.risk.RiskLevel
import java.io.Serializable

data class AnalysisResult(
    val appName: String,
    val permissions: List<String>,
    val riskScore: Int,
    val riskLevel: RiskLevel
) : Serializable
