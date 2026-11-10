package com.example.puppet.domain

interface ModuleRepository {
    /** Страница каталога. Пустой [query] — весь каталог по убыванию загрузок. */
    suspend fun getModulesPage(query: String, offset: Int): ModulesPage

    /** Модуль по slug или `null`, если такого в источнике нет. */
    suspend fun getModule(slug: String): PuppetModule?

    /** Соседи модуля по числу загрузок среди уже загруженных в память модулей. */
    suspend fun getNeighbours(slug: String): List<PuppetModule>
}

fun List<PuppetModule>.neighboursOf(slug: String): List<PuppetModule> {
    val sorted = sortedByDescending { it.downloads }
    val index = sorted.indexOfFirst { it.slug == slug }
    if (index == -1) return emptyList()
    return sorted.subList(maxOf(0, index - 1), minOf(sorted.size, index + 2))
}
