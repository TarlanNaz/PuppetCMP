package com.example.crypto.data

import com.example.crypto.domain.ModuleRepository
import com.example.crypto.domain.PuppetModule

class ModuleRepositoryImpl : ModuleRepository {
    override suspend fun getModules(): List<PuppetModule> = mockModules

    override suspend fun getModule(slug: String): PuppetModule? =
        mockModules.find { it.slug == slug }
}
