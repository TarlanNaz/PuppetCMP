package com.example.puppet.ui.model

import com.example.puppet.domain.ModuleRepository
import com.example.puppet.domain.PuppetModule
import com.example.puppet.domain.getModules

data class ModuleCardUi(
    val slug: String,
    val name: String,
    val owner: String,
    val downloads: String,
    val initial: String,
)

fun PuppetModule.toCardUi(): ModuleCardUi = ModuleCardUi(
    slug = slug,
    name = name,
    owner = owner,
    downloads = displayDownloads(downloads),
    initial = displayInitial(name),
)

fun List<PuppetModule>.toCardsUi(): List<ModuleCardUi> = map { it.toCardUi() }

suspend fun ModuleRepository.getCards(query: String?): List<ModuleCardUi> =
    getModules(query).toCardsUi()
