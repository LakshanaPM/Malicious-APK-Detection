package com.project.apkdetection.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.project.apkdetection.R
import com.project.apkdetection.filehandler.ApkFileManager
import com.project.apkdetection.analyzer.ApkMetadataAnalyzer
import com.project.apkdetection.security.PermissionAnalyzer
import com.project.apkdetection.security.CertificateAnalyzer
import com.project.apkdetection.security.AbuseDetector
import com.project.apkdetection.risk.RiskScorer
import com.project.apkdetection.risk.RiskClassifier
import com.project.apkdetection.model.AnalysisResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSelectApk = findViewById<Button>(R.id.btnSelectApk)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnSelectApk.setOnClickListener {
            tvStatus.text = getString(R.string.status_opening_picker)

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/vnd.android.package-archive"
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && data != null) {

            val apkUri = data.data ?: return
            Log.d("APK_SCAN", "APK selected: $apkUri")

            val apkFile = ApkFileManager.copyApkToCache(this, apkUri)
                ?: return

            val apkInfo = ApkMetadataAnalyzer.analyze(this, apkFile)
            Log.d("APK_SCAN", "App Name: ${apkInfo.appName}")
            Log.d("APK_SCAN", "Total Permissions: ${apkInfo.permissions.size}")

            val dangerousPerms =
                PermissionAnalyzer.findDangerousPermissions(apkInfo.permissions)
            Log.d("APK_SCAN", "Dangerous Permissions: ${dangerousPerms.size}")

            val certResult = CertificateAnalyzer.analyze(this, apkFile)
            Log.d("APK_SCAN", "Certificate signed: ${certResult.isSigned}")
            Log.d("APK_SCAN", "Debug certificate: ${certResult.isDebug}")

            val abuseResult = AbuseDetector.detect(apkInfo.permissions)
            Log.d("APK_SCAN", "Overlay abuse: ${abuseResult.hasOverlay}")
            Log.d("APK_SCAN", "SMS abuse: ${abuseResult.hasSmsAccess}")

            val totalPermissions = apkInfo.permissions.size
            val dangerousPermissions = dangerousPerms.size

            val riskScore = RiskScorer().calculate(
                totalPermissions,
                dangerousPermissions,
                certResult,
                abuseResult
            )

            val riskLevel = RiskClassifier.classify(riskScore)

            Log.d("APK_SCAN", "Final Risk Score: $riskScore")
            Log.d("APK_SCAN", "Risk Level: $riskLevel")

            val result = AnalysisResult(
                apkInfo.appName,
                apkInfo.permissions.toList(),
                riskScore,
                riskLevel
            )

            val resultIntent = Intent(this, ResultActivity::class.java)
            resultIntent.putExtra("RESULT", result)
            startActivity(resultIntent)
        }
    }
}
