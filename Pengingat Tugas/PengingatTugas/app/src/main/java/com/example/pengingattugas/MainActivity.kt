package com.example.pengingattugas

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val mintaIzin = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { diberikan ->
        if (!diberikan) {
            Toast.makeText(this, "Notifikasi dimatikan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mintaIzin.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM", "Token: ${task.result}")
            }
        }

        tampilkanStatusTugas(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        tampilkanStatusTugas(intent)
    }

    private fun tampilkanStatusTugas(sourceIntent: Intent?) {
        val txtStatus = findViewById<TextView>(R.id.txtStatus)
        val tugasId = sourceIntent?.getStringExtra("tugas_id")
        txtStatus.text = if (!tugasId.isNullOrEmpty()) {
            "Buka tugas $tugasId"
        } else {
            "Menunggu pengingat tugas"
        }
    }
}