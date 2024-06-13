package com.trucks.app.monitor.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trucks.app.monitor.R
import data.TruckItem

@Composable
fun TruckViewItem(car: TruckItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp),
         elevation = CardDefaults.cardElevation(4.dp)

    ) {
        Column {
            Row {
                Text( modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.vehicle_id)
                )
                Text( modifier = Modifier.padding(4.dp),
                    text = car.vehicleId
                )
            }
            Row {
                Text( modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.in_range)
                )
                Canvas(modifier = Modifier.size(36.dp).padding(8.dp)) {
                    val canvasWidth = 36F
                    val canvasHeight = 36F

                    drawCircle(
                        color = if (car.inRange) Color.Green else Color.Red,
                        center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                        radius = size.minDimension/2
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 500, heightDp = 150, showBackground = true)
@Composable
fun SimpleComposablePreview() {
    TruckViewItem(TruckItem(vehicleId = "V123", inRange = false))
}