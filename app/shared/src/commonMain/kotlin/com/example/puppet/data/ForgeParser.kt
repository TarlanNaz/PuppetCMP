package com.example.puppet.data

import com.example.puppet.data.dto.ModuleDto
import com.example.puppet.data.dto.ModulesResponseDto
import com.example.puppet.domain.ModulesPage
import com.example.puppet.domain.PuppetModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/** Разбор ответов API и перевод моделей ответа в доменные. Чистые функции без сети. */
internal class ForgeParser(private val json: Json) {

    fun parsePage(body: JsonElement): ModulesPage {
        val response = json.decodeFromJsonElement(ModulesResponseDto.serializer(), body)
        val pagination = response.pagination
        return ModulesPage(
            items = response.results.map { it.toDomain() },
            nextOffset = if (pagination.next != null) pagination.offset + pagination.limit else null,
            total = pagination.total,
        )
    }

    fun parseModule(body: JsonElement): PuppetModule =
        json.decodeFromJsonElement(ModuleDto.serializer(), body).toDomain()
}

internal fun ModuleDto.toDomain(): PuppetModule = PuppetModule(
    slug = slug,
    name = name,
    owner = owner?.username ?: slug.substringBefore('-'),
    version = currentRelease?.version.orEmpty(),
    downloads = downloads,
    summary = currentRelease?.metadata?.summary.stringOrEmpty(),
    license = currentRelease?.metadata?.license.stringOrEmpty(),
    tags = currentRelease?.tags.orEmpty(),
    updatedAt = updatedAt.orEmpty(),
)

private fun JsonElement?.stringOrEmpty(): String =
    (this as? JsonPrimitive)?.contentOrNull.orEmpty()
