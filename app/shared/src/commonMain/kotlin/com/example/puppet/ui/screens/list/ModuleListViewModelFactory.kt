package com.example.puppet.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.ui.navigation.Navigator
import kotlin.reflect.KClass

class ModuleListViewModelFactory(
    private val navigator: Navigator,
    private val repository: ModuleRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return ModuleListViewModel(navigator = navigator, repository = repository) as T
    }
}
