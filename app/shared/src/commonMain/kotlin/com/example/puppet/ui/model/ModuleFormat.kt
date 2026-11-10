package com.example.puppet.ui.model

internal fun displayInitial(name: String): String = name.take(1).uppercase()

internal fun displayDownloads(downloads: Long): String =
    downloads.toString().reversed().chunked(3).joinToString(" ").reversed()

// API отдаёт дату как «2023-08-30 03:11:12 -0700»; на случай ISO-формата режем и по «T».
internal fun displayDate(date: String): String =
    date.substringBefore('T').substringBefore(' ').ifEmpty { MISSING }

internal const val MISSING = "—"
