package com.example.puppet.ui.model

import com.example.puppet.domain.PuppetModule

data class ModuleDetailUi(
    val slug: String,
    val name: String,
    val initial: String,
    val owner: String,
    val version: String,
    val downloads: String,
    val summary: String,
    val license: String,
    val updatedAt: String,
    val tags: List<String>,
    val neighbours: List<NeighbourUi>,
)

data class NeighbourUi(
    val slug: String,
    val name: String,
    val initial: String,
    val current: Boolean,
)

fun PuppetModule.toDetailUi(neighbours: List<PuppetModule>): ModuleDetailUi = ModuleDetailUi(
    slug = slug,
    name = name,
    initial = displayInitial(name),
    owner = owner,
    version = version,
    downloads = displayDownloads(downloads),
    summary = summary,
    license = license,
    updatedAt = displayDate(updatedAt),
    tags = tags,
    neighbours = neighbours.map { other ->
        NeighbourUi(
            slug = other.slug,
            name = other.name,
            initial = displayInitial(other.name),
            current = other.slug == slug,
        )
    },
)
