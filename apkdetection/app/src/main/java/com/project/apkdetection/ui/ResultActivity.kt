package com.project.apkdetection.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.apkdetection.R
import com.project.apkdetection.model.AnalysisResult
import com.project.apkdetection.risk.RiskLevel

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Views
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvRiskLevel = findViewById<TextView>(R.id.tvRiskLevel)
        val tvRiskScore = findViewById<TextView>(R.id.tvRiskScore)
        val tvCertificate = findViewById<TextView>(R.id.tvCertificate)
        val tvAbuseWarning = findViewById<TextView>(R.id.tvAbuseWarning)
        val tvPermissions = findViewById<TextView>(R.id.tvPermissions)

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnProceed = findViewById<Button>(R.id.btnProceed)

        // Get result from intent
        val result = intent.getSerializableExtra("RESULT") as? AnalysisResult
            ?: run {
                finish()
                return
            }

        // Set values
        tvAppName.text = result.appName
        tvRiskScore.text = getString(R.string.risk_score, result.riskScore)
        tvRiskLevel.text = result.riskLevel.name
        tvCertificate.text = getString(R.string.certificate_info)

        tvPermissions.text =
            result.permissions.joinToString(separator = "\n") { "- $it" }

        // Abuse warning visibility
        tvAbuseWarning.visibility =
            if (result.riskLevel == RiskLevel.HIGH)
                TextView.VISIBLE
            else
                TextView.GONE

        // Color-code risk level
        when (result.riskLevel) {
            RiskLevel.HIGH ->
                tvRiskLevel.setBackgroundResource(R.drawable.bg_risk_high)

            RiskLevel.SUSPICIOUS ->
                tvRiskLevel.setBackgroundResource(R.drawable.bg_risk_medium)

            RiskLevel.SAFE ->
                tvRiskLevel.setBackgroundResource(R.drawable.bg_risk_safe)
        }

        // Buttons
        btnCancel.setOnClickListener { finish() }
        btnProceed.setOnClickListener { finish() }
    }
}
