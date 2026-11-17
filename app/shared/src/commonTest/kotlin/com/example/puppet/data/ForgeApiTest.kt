package com.example.puppet.data

import com.example.puppet.data.network.ForgeApi
import com.example.puppet.data.network.configureForgeClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class ForgeApiTest {
    private val json = createForgeJson()

    private fun api(status: HttpStatusCode, body: String): ForgeApi {
        val engine = MockEngine {
            respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        return ForgeApi(
            client = HttpClient(engine) { configureForgeClient(json) },
            baseUrl = "https://forge.test",
        )
    }

    @Test
    fun clientDeserializesJsonForParser() = runTest {
        val api = api(
            HttpStatusCode.OK,
            """{"slug":"owner-module","name":"module","downloads":null,"unknown":true}""",
        )
        try {
            val module = ForgeParser(json).parseModule(requireNotNull(api.module("owner-module")))
            assertEquals("owner-module", module.slug)
            assertEquals(0L, module.downloads)
        } finally {
            api.close()
        }
    }

    @Test
    fun missingModuleReturnsNullEvenWithNonJsonErrorBody() = runTest {
        val api = api(HttpStatusCode.NotFound, "Module not found")
        try {
            assertNull(api.module("missing-module"))
        } finally {
            api.close()
        }
    }

    @Test
    fun otherClientErrorsArePropagated() = runTest {
        val api = api(HttpStatusCode.Forbidden, "Forbidden")
        try {
            assertFailsWith<ClientRequestException> { api.module("owner-module") }
        } finally {
            api.close()
        }
    }

    @Test
    fun serverErrorsArePropagated() = runTest {
        val api = api(HttpStatusCode.InternalServerError, "Server error")
        try {
            assertFailsWith<ServerResponseException> { api.module("owner-module") }
        } finally {
            api.close()
        }
    }
}
