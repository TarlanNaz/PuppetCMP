package com.example.crypto

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crypto.data.ModuleRepositoryImpl
import com.example.crypto.domain.ModuleRepository
import com.example.crypto.resources.Res
import com.example.crypto.resources.action_toggle_theme
import com.example.crypto.ui.AppTheme
import com.example.crypto.ui.components.AppScaffold
import com.example.crypto.ui.navigation.AppNavDisplay
import com.example.crypto.ui.navigation.Navigator
import com.example.crypto.ui.screens.detail.ModuleDetailViewModelFactory
import com.example.crypto.ui.screens.list.ModuleListViewModelFactory
import org.jetbrains.compose.resources.stringResource

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }

    AppTheme(darkTheme) {
        val repository: ModuleRepository = remember { ModuleRepositoryImpl() }
        val navigator = remember { Navigator() }
        val listViewModelFactory = remember { ModuleListViewModelFactory(navigator, repository) }
        val detailViewModelFactory = remember { ModuleDetailViewModelFactory(navigator, repository) }
        val navStack = navigator.navStack.collectAsStateWithLifecycle()
        val backNavigationIsAvailable by remember { derivedStateOf { navStack.value.size > 1 } }

        AppScaffold(
            onBack = navigator::back.takeIf { backNavigationIsAvailable },
            actions = {
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
                listViewModelFactory = listViewModelFactory,
                detailViewModelFactory = detailViewModelFactory,
            )
        }
    }
}
