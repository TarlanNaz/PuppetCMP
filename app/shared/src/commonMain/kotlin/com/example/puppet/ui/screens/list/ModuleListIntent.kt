package com.example.puppet.ui.screens.list

sealed interface ModuleListIntent {
    data class CardClicked(val slug: String) : ModuleListIntent
    data class QueryChanged(val value: String) : ModuleListIntent
    data object EndReached : ModuleListIntent
    data object Retry : ModuleListIntent
}
