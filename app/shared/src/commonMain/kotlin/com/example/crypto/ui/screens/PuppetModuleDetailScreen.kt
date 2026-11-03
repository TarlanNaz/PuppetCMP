package com.example.crypto.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.data.PuppetModule
import com.example.crypto.ui.components.ModuleAvatar
import com.example.crypto.ui.components.ModuleCard
import crypto.app.shared.generated.resources.Res
import crypto.app.shared.generated.resources.back
import crypto.app.shared.generated.resources.description
import crypto.app.shared.generated.resources.downloads
import crypto.app.shared.generated.resources.other_modules
import crypto.app.shared.generated.resources.owner
import crypto.app.shared.generated.resources.version
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetModuleDetailScreen(
    module: PuppetModule,
    otherModules: List<PuppetModule>,
    onModuleClick: (PuppetModule) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(module.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModuleAvatar(module.name, size = 64.dp)
                Text(module.name, style = MaterialTheme.typography.headlineMedium)
            }
            Text(stringResource(Res.string.owner, module.owner.username))
            Text(stringResource(Res.string.version, module.currentRelease.version))
            Text(stringResource(Res.string.downloads, module.downloads.toString()))
            Text(
                stringResource(Res.string.description),
                style = MaterialTheme.typography.titleMedium
            )
            Text(module.currentRelease.metadata.summary)

            if (otherModules.isNotEmpty()) {
                Text(
                    stringResource(Res.string.other_modules),
                    style = MaterialTheme.typography.titleMedium
                )
                otherModules.forEach { other ->
                    ModuleCard(module = other, onClick = { onModuleClick(other) })
                }
            }
        }
    }
}
