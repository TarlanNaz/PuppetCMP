package com.example.puppet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.puppet.locale.AppEnvironment
import com.example.puppet.resources.Res
import com.example.puppet.resources.action_toggle_theme
import com.example.puppet.resources.language_switch
import com.example.puppet.ui.AppTheme
import com.example.puppet.ui.components.AppScaffold
import com.example.puppet.ui.navigation.AppNavDisplay
import org.jetbrains.compose.resources.stringResource

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf<String?>(null) }

    val graph = viewModel { AppGraph() }
    val navigator = graph.navigator
    val navStack = navigator.navStack.collectAsStateWithLifecycle()
    val backNavigationIsAvailable by remember { derivedStateOf { navStack.value.size > 1 } }

    AppEnvironment(language) {
        AppTheme(darkTheme) {
            AppScaffold(
                onBack = navigator::back.takeIf { backNavigationIsAvailable },
                actions = {
                    TextButton(
                        onClick = {
                            val current = language ?: Locale.current.language
                            language = if (current == "en") "ru" else "en"
                        },
                    ) {
                        Text(stringResource(Res.string.language_switch))
                    }
                    IconButton(onClick = { darkTheme = !darkTheme }) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = stringResource(Res.string.action_toggle_theme),
                        )
                    }
                },
            ) { modifier ->
                AppNavDisplay(
                    modifier = modifier,
                    navigator = navigator,
                    listViewModelFactory = graph.listViewModelFactory,
                    detailViewModelFactory = graph.detailViewModelFactory,
                )
            }
        }
    }
}
