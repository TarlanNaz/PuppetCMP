package com.example.puppet.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout

private const val REQUEST_TIMEOUT_MS = 20_000L

/** HTTP-клиент на движке конкретной платформы: OkHttp, Darwin или браузерный fetch. */
internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

internal fun createHttpClient(): HttpClient = platformHttpClient {
    // Ответы не 2xx превращаются в исключения, а не в «успешный» ответ с телом ошибки.
    expectSuccess = true
    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MS
    }
}
