package com.example.puppet

sealed interface Screen {
    data object List : Screen
    data class Detail(val slug: String) : Screen
}
