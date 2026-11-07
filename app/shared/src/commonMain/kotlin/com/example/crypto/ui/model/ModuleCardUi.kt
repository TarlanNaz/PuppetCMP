package com.example.crypto.ui.model

import com.example.crypto.domain.ModuleRepository
import com.example.crypto.domain.PuppetModule
import com.example.crypto.domain.getModules

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
