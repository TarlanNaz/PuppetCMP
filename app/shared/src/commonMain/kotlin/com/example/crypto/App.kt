package com.example.crypto

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.crypto.data.mockModules
import com.example.crypto.navigation.Route
import com.example.crypto.ui.screens.PuppetModuleDetailScreen
import com.example.crypto.ui.screens.PuppetModuleListScreen

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var isRussian by remember { mutableStateOf(false) }
    val backStack = remember { mutableStateListOf<Route>(Route.List) }

    val strings = if (isRussian) ruStrings else enStrings

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Route.List> {
                    PuppetModuleListScreen(
                        modules = mockModules,
                        strings = strings,
                        onModuleClick = { module -> backStack.add(Route.Detail(module.slug)) },
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        onLanguageClick = { isRussian = !isRussian }
                    )
                }
                entry<Route.Detail> { route ->
                    val module = mockModules.find { it.slug == route.slug }
                    if (module != null) {
                        PuppetModuleDetailScreen(
                            module = module,
                            strings = strings,
                            onBackClick = { backStack.removeLastOrNull() }
                        )
                    } else {
                        Text(strings.moduleNotFound)
                    }
                }
            },
            transitionSpec = {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            },
            popTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
        )
    }
}
