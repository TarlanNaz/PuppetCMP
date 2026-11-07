package com.example.crypto.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.crypto.domain.ModuleRepository
import com.example.crypto.ui.navigation.Navigator
import kotlin.reflect.KClass

class ModuleDetailViewModelFactory(
    private val navigator: Navigator,
    private val repository: ModuleRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val slug = checkNotNull(extras[SlugKey]) { "slug не передан в CreationExtras" }
        @Suppress("UNCHECKED_CAST")
        return ModuleDetailViewModel(
            slug = slug,
            navigator = navigator,
            repository = repository,
        ) as T
    }

    companion object {
        val SlugKey = CreationExtras.Key<String>()

        fun extrasFor(slug: String): CreationExtras =
            MutableCreationExtras().apply { set(SlugKey, slug) }
    }
}
