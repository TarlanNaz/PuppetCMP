package com.example.puppet

import androidx.lifecycle.ViewModel
import com.example.puppet.data.createModuleRepository
import com.example.puppet.domain.ModuleRepository
import com.example.puppet.ui.navigation.Navigator
import com.example.puppet.ui.screens.detail.ModuleDetailViewModelFactory
import com.example.puppet.ui.screens.list.ModuleListViewModelFactory

/**
 * Зависимости приложения. Живут столько же, сколько ViewModel экранов, которые ими
 * пользуются: переживают пересоздание окна или Activity, а когда хранилище ViewModel
 * очищается, репозиторий закрывается вместе с HTTP-клиентом.
 */
class AppGraph(
    repository: ModuleRepository = createModuleRepository(),
) : ViewModel(repository) {
    val navigator = Navigator()
    val listViewModelFactory = ModuleListViewModelFactory(navigator, repository)
    val detailViewModelFactory = ModuleDetailViewModelFactory(navigator, repository)
}
