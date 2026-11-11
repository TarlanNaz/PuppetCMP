package com.example.puppet.ui.screens.detail

import androidx.compose.runtime.Immutable
import com.example.puppet.ui.model.ModuleDetailUi
import com.example.puppet.ui.model.NeighbourUi

sealed interface ModuleDetailState {
    data object Loading : ModuleDetailState
    data object NotFound : ModuleDetailState
    data object Error : ModuleDetailState
    data class Loaded(
        val module: ModuleDetailUi,
        val neighbours: NeighboursState,
    ) : ModuleDetailState
}

/** Соседи грузятся отдельно: карточка модуля показывается, не дожидаясь их. */
@Immutable
sealed interface NeighboursState {
    data object Loading : NeighboursState
    data object Error : NeighboursState

    // Список после создания не меняется — каждое обновление кладёт новый.
    @Immutable
    data class Shown(val items: List<NeighbourUi>) : NeighboursState
}
