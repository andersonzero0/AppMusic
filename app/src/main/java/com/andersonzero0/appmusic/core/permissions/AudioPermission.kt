package com.andersonzero0.appmusic.core.permissions

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.andersonzero0.appmusic.MainActivity

@Composable
fun AudioPermission(callback: () -> Unit) {
    val context = LocalContext.current

    val audiosPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted)
            ActivityResultContracts.RequestPermission()
        else
            callback()
    }

    fun checkAudiosPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            callback()
        } else if (shouldShowRequestPermissionRationale(
                context as MainActivity,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) && shouldShowRequestPermissionRationale(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                context,
                "Necessário permissão o acesso á mídia",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                audiosPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                audiosPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        checkAudiosPermission()
    }
}