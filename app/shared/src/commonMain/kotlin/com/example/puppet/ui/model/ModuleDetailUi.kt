package com.example.puppet.ui.model

import androidx.compose.runtime.Immutable
import com.example.puppet.domain.PuppetModule

// Списки внутри после создания не меняются, поэтому модель честно помечена неизменяемой:
// так Compose может пропускать перерисовку при частых обновлениях состояния.
@Immutable
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
)

@Immutable
data class NeighbourUi(
    val slug: String,
    val name: String,
    val initial: String,
    val current: Boolean,
)

fun PuppetModule.toDetailUi(): ModuleDetailUi = ModuleDetailUi(
    slug = slug,
    name = name,
    initial = displayInitial(name),
    owner = owner,
    version = version.ifEmpty { MISSING },
    downloads = displayDownloads(downloads),
    summary = summary,
    license = license.ifEmpty { MISSING },
    updatedAt = displayDate(updatedAt),
    tags = tags,
)

fun List<PuppetModule>.toNeighboursUi(currentSlug: String): List<NeighbourUi> = map { other ->
    NeighbourUi(
        slug = other.slug,
        name = other.name,
        initial = displayInitial(other.name),
        current = other.slug == currentSlug,
    )
}
