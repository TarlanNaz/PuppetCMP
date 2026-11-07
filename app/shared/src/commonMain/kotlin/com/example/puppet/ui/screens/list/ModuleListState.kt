package com.example.puppet.ui.screens.list

import com.example.puppet.ui.model.ModuleCardUi

data class ModuleListState(
    val query: String = "",
    val items: List<ModuleCardUi> = emptyList(),
)
