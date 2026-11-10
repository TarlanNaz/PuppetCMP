package com.example.puppet.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.puppet.Screen
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.ui.model.toDetailUi
import com.example.puppet.ui.navigation.Navigator
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        load()
    }

    fun onIntent(intent: ModuleDetailIntent) {
        when (intent) {
            is ModuleDetailIntent.NeighbourClicked -> navigator.addToBackStack(Screen.Detail(intent.slug))

            ModuleDetailIntent.Retry -> if (_state.value == ModuleDetailState.Error) load()
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = ModuleDetailState.Loading
            try {
                val module = repository.getModule(slug)
                _state.value = if (module == null) {
                    ModuleDetailState.NotFound
                } else {
                    val neighbours = repository.getNeighbours(slug)
                    val ui = withContext(workDispatcher) { module.toDetailUi(neighbours) }
                    ModuleDetailState.Loaded(ui)
                }
            } catch (e: CancellationException) {
                // Ушли с экрана раньше ответа — это отмена, а не ошибка загрузки.
                throw e
            } catch (e: Exception) {
                _state.value = ModuleDetailState.Error
            }
        }
    }
}
