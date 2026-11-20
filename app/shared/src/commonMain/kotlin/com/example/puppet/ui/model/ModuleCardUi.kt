package com.example.puppet.ui.model

import androidx.compose.runtime.Immutable
import com.example.puppet.domain.PuppetModule

@Immutable
data class ModuleCardUi(
    val slug: String,
    val name: String,
    val owner: String,
    val downloads: String,
    val initial: String,
)

fun PuppetModule.toCardUi(): ModuleCardUi = ModuleCardUi(
    slug = slug,
    name = name,
    owner = owner,
    downloads = displayDownloads(downloads),
    initial = displayInitial(name),
)

fun List<PuppetModule>.toCardsUi(): List<ModuleCardUi> = map { it.toCardUi() }

/**
 * Склейка страниц: к уже показанным карточкам добавляется новая страница.
 * Пока листаем, каталог на сервере может сдвинуться, и модуль придёт дважды —
 * повторы отбрасываются, иначе в списке окажутся одинаковые ключи.
 */
fun List<ModuleCardUi>.appendPage(page: List<PuppetModule>): List<ModuleCardUi> {
    val known = mapTo(HashSet<String>(size)) { it.slug }
    val result = ArrayList<ModuleCardUi>(size + page.size)
    result.addAll(this)
    for (module in page) {
        if (known.add(module.slug)) result.add(module.toCardUi())
    }
    return result
}
