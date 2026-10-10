package com.example.crypto

import org.jetbrains.compose.resources.stringResource
import crypto.app.shared.generated.resources.*

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import com.example.crypto.data.PuppetModule
import com.example.crypto.data.mockModules
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object List : Route
    @Serializable
    data class Detail(val slug: String) : Route
}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.List::class, Route.List.serializer())
            subclass(Route.Detail::class, Route.Detail.serializer())
        }
    }
}

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        val backStack = rememberNavBackStack(config, Route.List)

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Route.List> {
                    PuppetModuleListScreen(
                        modules = mockModules,
                        onModuleClick = { module ->
                            backStack.add(Route.Detail(module.slug))
                        },
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it }
                    )
                }
                entry<Route.Detail> { detailRoute ->
                    val module = mockModules.find { it.slug == detailRoute.slug }
                    if (module != null) {
                        PuppetModuleDetailScreen(
                            module = module,
                            onBackClick = { backStack.removeLastOrNull() }
                        )
                    } else {
                        Text("Module not found")
                    }
                }
            },
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500))
            },
            popTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) togetherWith
                        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetModuleListScreen(
    modules: List<PuppetModule>,
    onModuleClick: (PuppetModule) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = onThemeChange
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            items(modules) { module ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onModuleClick(module) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = module.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(Res.string.module_by, module.owner.username),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetModuleDetailScreen(
    module: PuppetModule,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(module.name) },
                navigationIcon = {
                    Button(onClick = onBackClick) {
                        Text(stringResource(Res.string.back))
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
            Text(
                text = "${stringResource(Res.string.name)} ${module.name}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${stringResource(Res.string.owner)} ${module.owner.username}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "${stringResource(Res.string.version)} ${module.currentRelease.version}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "${stringResource(Res.string.downloads)} ${module.downloads}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(Res.string.description), style = MaterialTheme.typography.titleMedium)
            Text(text = module.currentRelease.metadata.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
