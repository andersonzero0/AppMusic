package com.andersonzero0.appmusic.core.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermission(callback: () -> Unit) {
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            callback()
        } else {
            Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkNotificationPermission() {
        val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.POST_NOTIFICATIONS
        } else {
            return
        }

        val permissionStatus = ContextCompat.checkSelfPermission(context, requiredPermission)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d("AudioPermission", "Permissão já concedida")
            callback()
        } else {
            Log.d("AudioPermission", "Permissão não concedida")
            if (context is Activity) {
                if (shouldShowRequestPermissionRationale(context, requiredPermission)) {
                    Toast.makeText(
                        context,
                        "Necessário permissão para acesso à mídia",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    notificationPermissionLauncher.launch(requiredPermission)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        checkNotificationPermission()
    }
}