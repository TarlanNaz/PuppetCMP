package com.example.crypto.navigation

sealed interface Route {
    data object List : Route
    data class Detail(val slug: String) : Route
}
