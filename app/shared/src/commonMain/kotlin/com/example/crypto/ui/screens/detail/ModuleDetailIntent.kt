package com.example.crypto.ui.screens.detail

sealed interface ModuleDetailIntent {
    data class NeighbourClicked(val slug: String) : ModuleDetailIntent
}
