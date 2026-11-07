package com.example.puppet.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.puppet.Screen
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.domain.neighboursOf
import com.example.puppet.ui.model.toDetailUi
import com.example.puppet.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModuleDetailViewModel(
    private val slug: String,
    private val navigator: Navigator,
    private val repository: ModuleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ModuleDetailState>(ModuleDetailState.Loading)
    val state: StateFlow<ModuleDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val module = repository.getModule(slug)
            _state.value = if (module == null) {
                ModuleDetailState.NotFound
            } else {
                val neighbours = repository.getModules().neighboursOf(slug)
                ModuleDetailState.Loaded(module.toDetailUi(neighbours))
            }
        }
    }

    fun onIntent(intent: ModuleDetailIntent) {
        when (intent) {
            is ModuleDetailIntent.NeighbourClicked -> navigator.addToBackStack(Screen.Detail(intent.slug))
        }
    }
}
