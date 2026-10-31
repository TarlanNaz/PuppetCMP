package com.example.crypto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.Strings
import com.example.crypto.data.PuppetModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetModuleDetailScreen(
    module: PuppetModule,
    strings: Strings,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(module.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("${strings.name} ${module.name}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${strings.owner} ${module.owner.username}")
            Text("${strings.version} ${module.currentRelease.version}")
            Text("${strings.downloads} ${module.downloads}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(strings.description, style = MaterialTheme.typography.titleMedium)
            Text(module.currentRelease.metadata.summary)
        }
    }
}
