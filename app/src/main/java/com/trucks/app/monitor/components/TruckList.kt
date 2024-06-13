package com.trucks.app.monitor.components

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import data.TruckItem

@Composable
fun TruckList(vehicles: List<TruckItem>) {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        HandleNotificationPermission(context)
    }

    LazyColumn {
        items(vehicles) { vehicle ->
            TruckViewItem(car = vehicle)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleNotificationPermission(context: Context) {
    val postNotificationPermission = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(postNotificationPermission) {
        if (!postNotificationPermission.hasPermission) {
            if (postNotificationPermission.shouldShowRationale) {
                Toast.makeText(context, "Notification permission is required to show notifications.", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTruckList() {
    val vehicles = listOf(
        TruckItem(vehicleId = "V001", inRange = true),
        TruckItem(vehicleId = "V002", inRange = false),
        TruckItem(vehicleId = "V003", inRange = true)
    )
    Surface {
        TruckList(vehicles = vehicles)
    }
}