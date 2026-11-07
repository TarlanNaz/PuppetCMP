package com.example.crypto

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.crypto.data.MockModuleRepository
import com.example.crypto.locale.AppEnvironment
import com.example.crypto.navigation.Route
import com.example.crypto.ui.ModuleListViewModel
import com.example.crypto.ui.screens.PuppetModuleDetailScreen
import com.example.crypto.ui.screens.PuppetModuleListScreen
import crypto.app.shared.generated.resources.Res
import crypto.app.shared.generated.resources.module_not_found
import org.jetbrains.compose.resources.stringResource

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("ru") }
    val repository = remember { MockModuleRepository() }
    val listViewModel = viewModel { ModuleListViewModel(repository) }
    val backStack = remember { mutableStateListOf<Route>(Route.List) }
    val listState = rememberLazyListState()

    AppEnvironment(language) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Route.List> {
                        PuppetModuleListScreen(
                            state = listViewModel.state,
                            listState = listState,
                            onQueryChange = listViewModel::onSearchQueryChange,
                            onModuleClick = { module -> backStack.add(Route.Detail(module.slug)) },
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { isDarkTheme = it },
                            onLanguageClick = { language = if (language == "ru") "en" else "ru" }
                        )
                    }
                    entry<Route.Detail> { route ->
                        val module = remember(route.slug) { repository.getModule(route.slug) }
                        if (module != null) {
                            PuppetModuleDetailScreen(
                                module = module,
                                otherModules = remember(route.slug) { repository.getRelatedModules(module) },
                                onModuleClick = { other -> backStack.add(Route.Detail(other.slug)) },
                                onBackClick = { backStack.removeLastOrNull() }
                            )
                        } else {
                            Surface(modifier = Modifier.fillMaxSize()) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(stringResource(Res.string.module_not_found))
                                }
                            }
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
}
