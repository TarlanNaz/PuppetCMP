package com.example.puppet.data

import kotlinx.serialization.json.Json

internal fun createForgeJson(): Json = Json {
    // Новые поля API не должны ломать приложение.
    ignoreUnknownKeys = true
    // null у необязательного поля заменяется его значением по умолчанию.
    coerceInputValues = true
}
