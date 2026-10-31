package com.example.crypto

class Strings(
    val appName: String,
    val moduleBy: String,
    val back: String,
    val name: String,
    val owner: String,
    val version: String,
    val downloads: String,
    val description: String,
    val moduleNotFound: String,
    val searchHint: String,
    val search: String,
    val clear: String,
    val language: String
)

val enStrings = Strings(
    appName = "Puppet Modules",
    moduleBy = "by",
    back = "Back",
    name = "Name:",
    owner = "Owner:",
    version = "Version:",
    downloads = "Downloads:",
    description = "Description:",
    moduleNotFound = "Module not found",
    searchHint = "Search modules...",
    search = "Search",
    clear = "Clear",
    language = "RU"
)

val ruStrings = Strings(
    appName = "Модули Puppet",
    moduleBy = "от",
    back = "Назад",
    name = "Название:",
    owner = "Владелец:",
    version = "Версия:",
    downloads = "Загрузки:",
    description = "Описание:",
    moduleNotFound = "Модуль не найден",
    searchHint = "Поиск модулей...",
    search = "Поиск",
    clear = "Очистить",
    language = "EN"
)
