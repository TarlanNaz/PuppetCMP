package com.example.puppet.ui.screens.detail

import androidx.compose.runtime.Immutable
import com.example.puppet.ui.model.ModuleDetailUi
import com.example.puppet.ui.model.NeighbourUi

@Immutable
sealed interface ModuleDetailState {
    data object Loading : ModuleDetailState
    /** API вернул HTTP 404: модуля с таким slug нет. */
    data object NotFound : ModuleDetailState
    /** Сбой сети, сервера или разбора ответа: экран предлагает повторить запрос. */
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
