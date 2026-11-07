package com.example.crypto.ui.model

internal fun displayInitial(name: String): String = name.take(1).uppercase()

internal fun displayDownloads(downloads: Long): String =
    downloads.toString().reversed().chunked(3).joinToString(" ").reversed()

internal fun displayDate(isoDate: String): String = isoDate.substringBefore('T')
