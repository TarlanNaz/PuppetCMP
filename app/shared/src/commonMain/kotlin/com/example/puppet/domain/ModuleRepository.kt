package com.example.puppet.domain

/** Источник модулей. Держит сетевые ресурсы, поэтому закрывается вместе с приложением. */
interface ModuleRepository : AutoCloseable {
    /** Страница каталога. Пустой [query] — весь каталог по убыванию загрузок. */
    suspend fun getModulesPage(query: String, offset: Int): ModulesPage

    /** Модуль по slug или `null`, если такого в источнике нет. */
    suspend fun getModule(slug: String): PuppetModule?

    /**
     * Соседи модуля в рейтинге всего каталога по числу загрузок: сам модуль и по одному
     * модулю выше и ниже него (у первого в каталоге соседа выше нет).
     */
    suspend fun getNeighbours(slug: String): List<PuppetModule>
}

fun List<PuppetModule>.neighboursOf(slug: String): List<PuppetModule> {
    val sorted = sortedByDescending { it.downloads }
    val index = sorted.indexOfFirst { it.slug == slug }
    if (index == -1) return emptyList()
    return sorted.subList(maxOf(0, index - 1), minOf(sorted.size, index + 2))
}
