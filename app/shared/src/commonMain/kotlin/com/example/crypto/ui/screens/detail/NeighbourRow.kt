package com.example.crypto.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.ui.components.ModuleAvatar
import com.example.crypto.ui.model.NeighbourUi

@Composable
fun NeighbourRow(
    neighbours: List<NeighbourUi>,
    onClick: (String) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        neighbours.forEach { neighbour ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(96.dp)
                    .clickable(enabled = !neighbour.current) { onClick(neighbour.slug) },
            ) {
                ModuleAvatar(initial = neighbour.initial, size = 56.dp, highlighted = neighbour.current)
                Text(
                    neighbour.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (neighbour.current) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}
