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
fun AudioPermission(callback: () -> Unit) {
    val context = LocalContext.current

    val audiosPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            callback()
        } else {
            Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAudiosPermission() {
        val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
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
                    audiosPermissionLauncher.launch(requiredPermission)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        checkAudiosPermission()
    }
}