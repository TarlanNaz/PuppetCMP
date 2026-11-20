package com.example.puppet.domain

data class ModulesPage(
    val items: List<PuppetModule>,
    /** Смещение следующей страницы или `null`, если страниц больше нет. */
    val nextOffset: Int?,
    /** Сколько всего записей у этой выборки на сервере, если API это сообщил. */
    val total: Int? = null,
)
