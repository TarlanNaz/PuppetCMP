package com.example.puppet.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import kotlinx.serialization.json.JsonElement

/**
 * Запросы к API каталога. HTTP-клиент читает JSON через ContentNegotiation,
 * а ForgeParser преобразует дерево JSON в DTO и доменные модели.
 */
internal class ForgeApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun modules(query: String, limit: Int, offset: Int): JsonElement =
        client.get {
            url {
                takeFrom(baseUrl)
                appendPathSegments("v3", "modules")
            }
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("exclude_fields", EXCLUDED_FIELDS)
            if (query.isEmpty()) {
                parameter("sort_by", "downloads")
            } else {
                // С поисковым запросом API само сортирует по релевантности.
                parameter("query", query)
            }
        }.body<JsonElement>()

    /** JSON ответа или `null`, если модуля нет (404). */
    suspend fun module(slug: String): JsonElement? =
        try {
            client.get {
                url {
                    takeFrom(baseUrl)
                    appendPathSegments("v3", "modules", slug)
                }
                parameter("exclude_fields", EXCLUDED_FIELDS)
            }.body<JsonElement>()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) null else throw e
        }

    fun close() = client.close()

    private companion object {
        // Список релизов и тексты README, CHANGELOG, лицензии и справки экрану не нужны,
        // а весят больше всего остального ответа. Поля перечисляются через пробел.
        const val EXCLUDED_FIELDS = "releases readme changelog license reference"
    }
}
