package com.example.crypto.ui.screens.detail

import com.example.crypto.ui.model.ModuleDetailUi

sealed interface ModuleDetailState {
    data object Loading : ModuleDetailState
    data object NotFound : ModuleDetailState
    data class Loaded(val module: ModuleDetailUi) : ModuleDetailState
}
