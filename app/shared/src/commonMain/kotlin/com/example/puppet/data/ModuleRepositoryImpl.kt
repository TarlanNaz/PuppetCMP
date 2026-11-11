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

    // Всё, что лежит ниже, хранится только в памяти. На диск ничего не пишется.
    private val cacheMutex = Mutex()
    private val cache = mutableMapOf<String, PuppetModule>()

    // Начало рейтинга каталога по загрузкам: страницы списка без поискового запроса,
    // загруженные подряд с нулевого смещения. По нему соседи находятся без лишних запросов.
    private var ranking: List<PuppetModule> = emptyList()
    private var rankingNextOffset: Int? = null
    private var rankingComplete = false

    override suspend fun getModulesPage(query: String, offset: Int): ModulesPage {
        val trimmed = query.trim()
        val page = fetchPage(query = trimmed, limit = PAGE_SIZE, offset = offset)
        if (trimmed.isEmpty()) extendRanking(offset, page)
        return page
    }

    override suspend fun getModule(slug: String): PuppetModule? {
        val body = api.moduleJson(slug) ?: return null
        val module = withContext(workDispatcher) { ForgeParser.parseModule(body) }
        remember(listOf(module))
        return module
    }

    override suspend fun getNeighbours(slug: String): List<PuppetModule> {
        val module = cacheMutex.withLock { cache[slug] } ?: getModule(slug) ?: return emptyList()
        return neighboursFromRanking(slug) ?: neighboursFromCatalog(module)
    }

    override fun close() {
        api.close()
    }

    /**
     * Соседи по уже загруженному началу рейтинга. `null`, если модуля там нет или он
     * последний из загруженных, а каталог не кончился: тогда соседа ниже мы ещё не знаем.
     */
    private suspend fun neighboursFromRanking(slug: String): List<PuppetModule>? = cacheMutex.withLock {
        val index = ranking.indexOfFirst { it.slug == slug }
        val lowerIsKnown = index < ranking.lastIndex || rankingComplete
        if (index == -1 || !lowerIsKnown) {
            null
        } else {
            ranking.subList(maxOf(0, index - 1), minOf(ranking.size, index + 2)).toList()
        }
    }

    /**
     * Соседи по всему каталогу. Каталог на сервере отсортирован по убыванию загрузок,
     * поэтому позицию модуля можно найти двоичным поиском по смещению: каждый шаг —
     * запрос одной записи (для нескольких тысяч модулей это около 13 коротких запросов).
     * После этого одним запросом берётся окно вокруг найденной позиции.
     */
    private suspend fun neighboursFromCatalog(module: PuppetModule): List<PuppetModule> {
        val total = fetchPage(query = "", limit = 1, offset = 0).total ?: return listOf(module)

        // low — сколько модулей в каталоге загружали чаще, чем этот.
        var low = 0
        var high = total
        var steps = 0
        while (low < high && steps < MAX_SEARCH_STEPS) {
            val middle = low + (high - low) / 2
            val downloads = fetchPage(query = "", limit = 1, offset = middle).items.firstOrNull()?.downloads
            if (downloads != null && downloads > module.downloads) low = middle + 1 else high = middle
            steps++
        }

        val window = fetchPage(query = "", limit = WINDOW_SIZE, offset = maxOf(0, low - WINDOW_SIZE / 2)).items
        return withContext(workDispatcher) {
            // Пока шёл поиск, каталог мог сдвинуться, поэтому модуль ставится в окно заново.
            (window.filter { it.slug != module.slug } + module).neighboursOf(module.slug)
        }
    }

    private suspend fun fetchPage(query: String, limit: Int, offset: Int): ModulesPage {
        val body = api.modulesJson(query = query, limit = limit, offset = offset)
        val page = withContext(workDispatcher) { ForgeParser.parsePage(body) }
        remember(page.items)
        return page
    }

    /** Продолжает рейтинг, если страница пришла ровно с того места, где он кончается. */
    private suspend fun extendRanking(offset: Int, page: ModulesPage) = withContext(workDispatcher) {
        cacheMutex.withLock {
            val continues = offset == 0 || (offset == rankingNextOffset && ranking.isNotEmpty())
            if (continues) {
                val base = if (offset == 0) emptyList() else ranking
                val known = base.mapTo(HashSet()) { it.slug }
                ranking = base + page.items.filter { known.add(it.slug) }
                rankingNextOffset = page.nextOffset
                rankingComplete = page.nextOffset == null
            }
        }
    }

    private suspend fun remember(modules: List<PuppetModule>) {
        cacheMutex.withLock {
            modules.forEach { cache[it.slug] = it }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val WINDOW_SIZE = 5
        // 2^20 больше любого реального каталога: защита от бесконечного цикла.
        const val MAX_SEARCH_STEPS = 20
    }
}
