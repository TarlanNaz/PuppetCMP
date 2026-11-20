package com.example.puppet.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.puppet.Screen
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.ui.model.appendPage
import com.example.puppet.ui.model.toCardsUi
import com.example.puppet.ui.navigation.Navigator
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModuleListViewModel(
    private val navigator: Navigator,
    private val repository: ModuleRepository,
    private val workDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow(ModuleListState())
    val state: StateFlow<ModuleListState> = _state.asStateFlow()

    // Новая первая страница отменяет и прежний поиск, и идущую догрузку.
    private var firstPageJob: Job? = null
    private var nextPageJob: Job? = null

    // Чем продолжать список: запрос, которым он загружен, и смещение следующей страницы.
    private var loadedQuery: String = ""
    private var nextOffset: Int? = null

    init {
        loadFirstPage(debounce = false)
    }

    fun onIntent(intent: ModuleListIntent) {
        when (intent) {
            is ModuleListIntent.CardClicked -> navigator.addToBackStack(Screen.Detail(intent.slug))

            is ModuleListIntent.QueryChanged -> {
                if (intent.value == _state.value.query) return
                _state.update { it.copy(query = intent.value) }
                loadFirstPage(debounce = true)
            }

            ModuleListIntent.EndReached -> {
                if (_state.value.append == AppendStatus.Idle) loadNextPage()
            }

            ModuleListIntent.Retry -> {
                val current = _state.value
                when {
                    current.status == ListStatus.Error -> loadFirstPage(debounce = false)
                    current.append == AppendStatus.Error -> loadNextPage()
                }
            }
        }
    }

    private fun loadFirstPage(debounce: Boolean) {
        firstPageJob?.cancel()
        nextPageJob?.cancel()
        // Старый список больше не продолжаем, даже пока он ещё на экране.
        nextOffset = null
        firstPageJob = viewModelScope.launch {
            // Пока пользователь печатает, каждая буква отменяет предыдущий запуск здесь же.
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            val query = _state.value.query
            _state.update {
                it.copy(items = emptyList(), status = ListStatus.Loading, append = AppendStatus.Idle)
            }
            try {
                val page = repository.getModulesPage(query = query, offset = 0)
                val cards = withContext(workDispatcher) { page.items.toCardsUi() }
                loadedQuery = query
                nextOffset = page.nextOffset
                _state.update {
                    it.copy(
                        items = cards,
                        status = ListStatus.Content,
                        append = if (page.nextOffset == null) AppendStatus.End else AppendStatus.Idle,
                    )
                }
            } catch (e: CancellationException) {
                // Отмена (новый запрос, уход с экрана) — не ошибка: пробрасываем дальше.
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(status = ListStatus.Error) }
            }
        }
    }

    private fun loadNextPage() {
        val offset = nextOffset
        if (_state.value.status != ListStatus.Content || offset == null) return
        val query = loadedQuery

        _state.update { it.copy(append = AppendStatus.Loading) }
        nextPageJob = viewModelScope.launch {
            try {
                val page = repository.getModulesPage(query = query, offset = offset)
                // Склейка страниц и перевод в UI-модели — не на UI-диспетчере.
                val merged = withContext(workDispatcher) { _state.value.items.appendPage(page.items) }
                nextOffset = page.nextOffset
                _state.update {
                    it.copy(
                        items = merged,
                        append = if (page.nextOffset == null) AppendStatus.End else AppendStatus.Idle,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(append = AppendStatus.Error) }
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
