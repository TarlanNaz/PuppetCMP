package com.example.crypto.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.Screen
import com.example.crypto.domain.ModuleRepository
import com.example.crypto.ui.model.getCards
import com.example.crypto.ui.navigation.Navigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ModuleListViewModel(
    private val navigator: Navigator,
    private val repository: ModuleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ModuleListState())
    val state: StateFlow<ModuleListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        search(query = null)
    }

    private fun search(query: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val items = repository.getCards(query)
            _state.update { it.copy(items = items) }
        }
    }

    fun onIntent(intent: ModuleListIntent) {
        when (intent) {
            is ModuleListIntent.CardClicked -> navigator.addToBackStack(Screen.Detail(intent.slug))

            is ModuleListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.value) }
                search(query = intent.value)
            }
        }
    }
}
