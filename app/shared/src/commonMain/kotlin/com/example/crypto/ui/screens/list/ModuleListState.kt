package com.example.crypto.ui.screens.list

import com.example.crypto.ui.model.ModuleCardUi

data class ModuleListState(
    val query: String = "",
    val items: List<ModuleCardUi> = emptyList(),
)
