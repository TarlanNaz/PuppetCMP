package com.example.puppet.data

import com.example.puppet.config.AppConfig
import com.example.puppet.data.network.ForgeApi
import com.example.puppet.data.network.createHttpClient
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.domain.ModulesPage
import com.example.puppet.domain.PuppetModule
import com.example.puppet.domain.neighboursOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/** Репозиторий на живых данных. Адрес API приходит из конфигурации сборки. */
fun createModuleRepository(): ModuleRepository =
    ModuleRepositoryImpl(api = ForgeApi(client = createHttpClient(), baseUrl = AppConfig.FORGE_BASE_URL))

internal class ModuleRepositoryImpl(
    private val api: ForgeApi,
    private val workDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ModuleRepository {

    // Кэш только в памяти: всё, что уже приходило из сети. На диск ничего не пишется.
    private val cacheMutex = Mutex()
    private val cache = mutableMapOf<String, PuppetModule>()

    override suspend fun getModulesPage(query: String, offset: Int): ModulesPage {
        val body = api.modulesJson(query = query.trim(), limit = PAGE_SIZE, offset = offset)
        val page = withContext(workDispatcher) { ForgeParser.parsePage(body) }
        remember(page.items)
        return page
    }

    override suspend fun getModule(slug: String): PuppetModule? {
        val body = api.moduleJson(slug) ?: return null
        val module = withContext(workDispatcher) { ForgeParser.parseModule(body) }
        remember(listOf(module))
        return module
    }

    override suspend fun getNeighbours(slug: String): List<PuppetModule> {
        val known = cacheMutex.withLock { cache.values.toList() }
        return withContext(workDispatcher) { known.neighboursOf(slug) }
    }

    private suspend fun remember(modules: List<PuppetModule>) {
        cacheMutex.withLock {
            modules.forEach { cache[it.slug] = it }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
