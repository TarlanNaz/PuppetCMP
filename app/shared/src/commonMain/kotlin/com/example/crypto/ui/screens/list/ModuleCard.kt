package com.example.crypto.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.ui.components.CardSurface
import com.example.crypto.ui.components.ModuleAvatar
import com.example.crypto.ui.model.ModuleCardUi
import com.example.crypto.resources.Res
import com.example.crypto.resources.module_downloads
import com.example.crypto.resources.module_owner
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModuleCard(module: ModuleCardUi, modifier: Modifier = Modifier) {
    CardSurface(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ModuleAvatar(initial = module.initial)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(module.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(Res.string.module_owner, module.owner),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    stringResource(Res.string.module_downloads, module.downloads),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
