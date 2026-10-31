package com.example.crypto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.Strings
import com.example.crypto.data.PuppetModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetModuleListScreen(
    modules: List<PuppetModule>,
    strings: Strings,
    onModuleClick: (PuppetModule) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLanguageClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredModules = modules.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.owner.username.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.appName) },
                actions = {
                    TextButton(onClick = onLanguageClick) {
                        Text(strings.language)
                    }
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = onThemeChange
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(strings.searchHint) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = strings.search)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = strings.clear)
                        }
                    }
                },
                singleLine = true
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredModules) { module ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onModuleClick(module) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(module.name, style = MaterialTheme.typography.titleMedium)
                            Text("${strings.moduleBy} ${module.owner.username}")
                        }
                    }
                }
            }
        }
    }
}
