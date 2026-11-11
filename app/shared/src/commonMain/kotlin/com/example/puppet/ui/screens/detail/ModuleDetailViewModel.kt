package com.example.puppet.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.puppet.Screen
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.ui.model.toDetailUi
import com.example.puppet.ui.model.toNeighboursUi
import com.example.puppet.ui.navigation.Navigator
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModuleDetailViewModel(
    private val slug: String,
    private val navigator: Navigator,
    private val repository: ModuleRepository,
    private val workDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow<ModuleDetailState>(ModuleDetailState.Loading)
    val state: StateFlow<ModuleDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null
    private var neighboursJob: Job? = null

    init {
        load()
    }

    fun onIntent(intent: ModuleDetailIntent) {
        when (intent) {
            is ModuleDetailIntent.NeighbourClicked -> navigator.addToBackStack(Screen.Detail(intent.slug))

            ModuleDetailIntent.Retry -> if (_state.value == ModuleDetailState.Error) load()

            ModuleDetailIntent.RetryNeighbours -> {
                val current = _state.value
                if (current is ModuleDetailState.Loaded && current.neighbours == NeighboursState.Error) {
                    loadNeighbours()
                }
            }
        }
    }

    private fun load() {
        loadJob?.cancel()
        neighboursJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = ModuleDetailState.Loading
            try {
                val module = repository.getModule(slug)
                if (module == null) {
                    _state.value = ModuleDetailState.NotFound
                } else {
                    val ui = withContext(workDispatcher) { module.toDetailUi() }
                    _state.value = ModuleDetailState.Loaded(ui, NeighboursState.Loading)
                    loadNeighbours()
                }
            } catch (e: CancellationException) {
                // Ушли с экрана раньше ответа — это отмена, а не ошибка загрузки.
                throw e
            } catch (e: Exception) {
                _state.value = ModuleDetailState.Error
            }
        }
    }

    private fun loadNeighbours() {
        neighboursJob?.cancel()
        neighboursJob = viewModelScope.launch {
            setNeighbours(NeighboursState.Loading)
            try {
                val neighbours = repository.getNeighbours(slug)
                val ui = withContext(workDispatcher) { neighbours.toNeighboursUi(slug) }
                setNeighbours(NeighboursState.Shown(ui))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Карточка модуля уже на экране — ошибка касается только блока соседей.
                setNeighbours(NeighboursState.Error)
            }
        }
    }

    private fun setNeighbours(neighbours: NeighboursState) {
        _state.update { current ->
            if (current is ModuleDetailState.Loaded) current.copy(neighbours = neighbours) else current
        }
    }
}
