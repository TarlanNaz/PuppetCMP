package com.example.puppet.domain

data class ModulesPage(
    val items: List<PuppetModule>,
    /** Смещение следующей страницы или `null`, если страниц больше нет. */
    val nextOffset: Int?,
)
