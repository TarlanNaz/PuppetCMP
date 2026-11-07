package com.example.puppet.domain

interface ModuleRepository {
    suspend fun getModules(): List<PuppetModule>
    suspend fun getModule(slug: String): PuppetModule?
}

suspend fun ModuleRepository.getModules(query: String?): List<PuppetModule> =
    getModules().filterByQuery(query)

fun List<PuppetModule>.filterByQuery(query: String?): List<PuppetModule> {
    val needle = query?.trim().orEmpty()
    if (needle.isEmpty()) return this
    return filter {
        it.name.contains(needle, ignoreCase = true) || it.owner.contains(needle, ignoreCase = true)
    }
}

fun List<PuppetModule>.neighboursOf(slug: String): List<PuppetModule> {
    val sorted = sortedByDescending { it.downloads }
    val index = sorted.indexOfFirst { it.slug == slug }
    if (index == -1) return emptyList()
    return sorted.subList(maxOf(0, index - 1), minOf(sorted.size, index + 2))
}
