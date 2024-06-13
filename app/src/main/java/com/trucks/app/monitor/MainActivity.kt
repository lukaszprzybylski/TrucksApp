package com.trucks.app.monitor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.trucks.app.monitor.components.TruckList
import com.trucks.app.monitor.ui.theme.TrucksAppMonitorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val truckViewModel: TruckViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        setContent {
            TrucksAppMonitorTheme {
                val vehicles by truckViewModel.vehicles.collectAsState()
                val hideNotification by truckViewModel.hideNotification.collectAsState()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    vehicles?.let { TruckList(vehicles = it) }
                }

                if (hideNotification) {
                    hideNotification()
                }
            }
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                truckViewModel.setAppInBackground(true)
                showNotification(truckViewModel.inRadiusCount)
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                truckViewModel.setAppInBackground(false)
            }
        })
    }

    private fun showNotification(inRadiusCount: Int) {
        val channelId = "vehicle_monitor_channel"
        val channelName = getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text, inRadiusCount))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun hideNotification() {
        notificationManager.cancelAll()
    }
}
