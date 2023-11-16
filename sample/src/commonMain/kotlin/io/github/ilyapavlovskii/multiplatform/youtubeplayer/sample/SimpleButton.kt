package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun RowScope.SimpleButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(8.dp),
        onClick = onClick,
    ) {
        Text(text = text)
    }
}