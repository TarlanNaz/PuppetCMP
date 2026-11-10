package com.example.puppet.ui.screens.detail

sealed interface ModuleDetailIntent {
    data class NeighbourClicked(val slug: String) : ModuleDetailIntent
    data object Retry : ModuleDetailIntent
}
