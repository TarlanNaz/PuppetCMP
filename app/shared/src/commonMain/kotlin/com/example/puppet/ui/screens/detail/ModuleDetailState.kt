package com.example.puppet.ui.screens.detail

import com.example.puppet.ui.model.ModuleDetailUi

sealed interface ModuleDetailState {
    data object Loading : ModuleDetailState
    data object NotFound : ModuleDetailState
    data object Error : ModuleDetailState
    data class Loaded(val module: ModuleDetailUi) : ModuleDetailState
}
