package com.example.crypto.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.crypto.data.ModuleRepository
import com.example.crypto.data.PuppetModule

data class ModuleListState(
    val query: String,
    val modules: List<PuppetModule>
)

class ModuleListViewModel(private val repository: ModuleRepository) : ViewModel() {
    var state by mutableStateOf(ModuleListState(query = "", modules = repository.getModules()))
        private set

    fun onSearchQueryChange(query: String) {
        state = ModuleListState(
            query = query,
            modules = repository.getModules().filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.owner.username.contains(query, ignoreCase = true)
            }
        )
    }
}
