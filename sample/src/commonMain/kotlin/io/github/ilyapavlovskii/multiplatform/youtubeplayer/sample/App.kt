package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        }
    }
}