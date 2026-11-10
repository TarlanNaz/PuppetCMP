package com.example.puppet.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

/**
 * Запросы к API каталога. Возвращает сырой текст ответа: разбор JSON — отдельный шаг,
 * который репозиторий выполняет не на UI-диспетчере.
 */
internal class ForgeApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun modulesJson(query: String, limit: Int, offset: Int): String =
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
        }.bodyAsText()

    /** Текст ответа или `null`, если модуля нет (404). */
    suspend fun moduleJson(slug: String): String? =
        try {
            client.get {
                url {
                    takeFrom(baseUrl)
                    appendPathSegments("v3", "modules", slug)
                }
                parameter("exclude_fields", EXCLUDED_FIELDS)
            }.bodyAsText()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) null else throw e
        }

    private companion object {
        // Список всех релизов экрану не нужен, а весит больше остального ответа.
        const val EXCLUDED_FIELDS = "releases"
    }
}
