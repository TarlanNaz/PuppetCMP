package com.example.puppet.ui.screens.list

import androidx.compose.runtime.Immutable
import com.example.puppet.ui.model.ModuleCardUi

// Список карточек после создания не меняется — каждое обновление кладёт новый список,
// поэтому состояние помечено неизменяемым и экран остаётся пропускаемым.
@Immutable
data class ModuleListState(
    val query: String = "",
    val items: List<ModuleCardUi> = emptyList(),
    val status: ListStatus = ListStatus.Loading,
    val append: AppendStatus = AppendStatus.Idle,
)

/** Что с первой страницей. */
enum class ListStatus { Loading, Content, Error }

/** Что с догрузкой следующих страниц. */
enum class AppendStatus { Idle, Loading, Error, End }
