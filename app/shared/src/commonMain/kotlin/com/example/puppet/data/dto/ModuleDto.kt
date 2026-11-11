package com.example.puppet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// Модели ответа API. Описаны только поля, которые нужны приложению;
// всё остальное пропускается (ignoreUnknownKeys). Почти всё необязательно:
// пустые поля API присылает как null.

@Serializable
internal data class ModulesResponseDto(
    val pagination: PaginationDto = PaginationDto(),
    val results: List<ModuleDto> = emptyList(),
)

@Serializable
internal data class PaginationDto(
    val limit: Int = 0,
    val offset: Int = 0,
    val next: String? = null,
    val total: Int? = null,
)

@Serializable
internal data class ModuleDto(
    val slug: String,
    val name: String,
    val downloads: Long = 0,
    @SerialName("updated_at") val updatedAt: String? = null,
    val owner: OwnerDto? = null,
    @SerialName("current_release") val currentRelease: ReleaseDto? = null,
)

@Serializable
internal data class OwnerDto(
    val username: String? = null,
)

@Serializable
internal data class ReleaseDto(
    val version: String? = null,
    val tags: List<String>? = null,
    val metadata: MetadataDto? = null,
)

// metadata.json пишут авторы модулей, поэтому типы полей не гарантированы:
// берём их как произвольный JSON и достаём строку уже в маппере.
@Serializable
internal data class MetadataDto(
    val summary: JsonElement? = null,
    val license: JsonElement? = null,
)
