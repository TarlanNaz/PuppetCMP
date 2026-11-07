package com.example.puppet.domain

data class PuppetModule(
    val slug: String,
    val name: String,
    val owner: String,
    val version: String,
    val downloads: Long,
    val summary: String,
    val license: String,
    val tags: List<String>,
    val updatedAt: String,
)
