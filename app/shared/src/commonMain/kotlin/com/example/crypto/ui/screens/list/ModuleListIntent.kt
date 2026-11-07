package com.example.crypto.ui.screens.list

sealed interface ModuleListIntent {
    data class CardClicked(val slug: String) : ModuleListIntent
    data class QueryChanged(val value: String) : ModuleListIntent
}
