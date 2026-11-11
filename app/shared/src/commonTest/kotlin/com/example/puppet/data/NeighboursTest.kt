package com.example.puppet.data

import com.example.puppet.data.network.ForgeApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest

class NeighboursTest {

    // Каталог из 1000 модулей, отсортированный по убыванию загрузок, как на сервере.
    private val catalog = List(1000) { rank -> "owner-m$rank" to (1_000_000L - rank * 100) }
    private var requests = 0

    private fun repository(): ModuleRepositoryImpl {
        val engine = MockEngine { request ->
            requests++
            val path = request.url.encodedPath
            val body = if (path.endsWith("/v3/modules")) {
                val offset = request.url.parameters["offset"]!!.toInt()
                val limit = request.url.parameters["limit"]!!.toInt()
                val items = catalog.drop(offset).take(limit)
                val next = if (offset + limit < catalog.size) "\"next\"" else "null"
                """{"pagination":{"limit":$limit,"offset":$offset,"total":${catalog.size},"next":$next},""" +
                    """"results":[${items.joinToString(",") { moduleJson(it) }}]}"""
            } else {
                moduleJson(catalog.first { it.first == path.substringAfterLast('/') })
            }
            respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val api = ForgeApi(client = HttpClient(engine), baseUrl = "https://forge.test")
        return ModuleRepositoryImpl(api = api, workDispatcher = Dispatchers.Unconfined)
    }

    private fun moduleJson(module: Pair<String, Long>) =
        """{"slug":"${module.first}","name":"${module.first}","downloads":${module.second},"unknown":1}"""

    @Test
    fun neighboursOfModuleFarFromLoadedListAreFoundInWholeCatalog() = runTest {
        val repository = repository()

        val neighbours = repository.getNeighbours("owner-m700").map { it.slug }

        assertEquals(listOf("owner-m699", "owner-m700", "owner-m701"), neighbours)
        assertTrue(requests <= 15, "двоичный поиск, а не перебор страниц: $requests запросов")
    }

    @Test
    fun neighboursInsideLoadedListNeedNoRequests() = runTest {
        val repository = repository()
        repository.getModulesPage(query = "", offset = 0)
        repository.getModule("owner-m5")
        val before = requests

        val neighbours = repository.getNeighbours("owner-m5").map { it.slug }

        assertEquals(listOf("owner-m4", "owner-m5", "owner-m6"), neighbours)
        assertEquals(before, requests)
    }

    @Test
    fun lastLoadedModuleStillGetsLowerNeighbour() = runTest {
        val repository = repository()
        repository.getModulesPage(query = "", offset = 0)

        val neighbours = repository.getNeighbours("owner-m19").map { it.slug }

        assertEquals(listOf("owner-m18", "owner-m19", "owner-m20"), neighbours)
    }

    @Test
    fun firstModuleInCatalogHasOnlyLowerNeighbour() = runTest {
        val neighbours = repository().getNeighbours("owner-m0").map { it.slug }

        assertEquals(listOf("owner-m0", "owner-m1"), neighbours)
    }
}
