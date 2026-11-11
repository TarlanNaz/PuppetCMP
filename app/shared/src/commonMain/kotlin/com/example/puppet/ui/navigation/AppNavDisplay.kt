package com.example.puppet.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.puppet.Screen
import com.example.puppet.ui.screens.detail.ModuleDetailScreen
import com.example.puppet.ui.screens.detail.ModuleDetailViewModel
import com.example.puppet.ui.screens.detail.ModuleDetailViewModelFactory
import com.example.puppet.ui.screens.list.ModuleListScreen
import com.example.puppet.ui.screens.list.ModuleListViewModel
import com.example.puppet.ui.screens.list.ModuleListViewModelFactory

private const val TRANSITION_MS = 300

@Composable
fun AppNavDisplay(
    modifier: Modifier,
    navigator: Navigator,
    listViewModelFactory: ModuleListViewModelFactory,
    detailViewModelFactory: ModuleDetailViewModelFactory,
) {
    val backStack by navigator.navStack.collectAsStateWithLifecycle()
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = navigator::back,
        transitionSpec = { slide(SlideDirection.Start) },
        popTransitionSpec = { slide(SlideDirection.End) },
        predictivePopTransitionSpec = { slide(SlideDirection.End) },
        entryProvider = entryProvider {
            entry<Screen.List> {
                val viewModel: ModuleListViewModel = viewModel(factory = listViewModelFactory)
                val state by viewModel.state.collectAsStateWithLifecycle()
                ModuleListScreen(state = state, onIntent = viewModel::onIntent)
            }
            entry<Screen.Detail> { key ->
                val viewModel: ModuleDetailViewModel = viewModel(
                    key = "detail-${key.slug}",
                    factory = detailViewModelFactory,
                    extras = ModuleDetailViewModelFactory.extrasFor(key.slug),
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                ModuleDetailScreen(state = state, onIntent = viewModel::onIntent)
            }
        },
    )
}

private fun AnimatedContentTransitionScope<*>.slide(
    direction: SlideDirection,
): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )
