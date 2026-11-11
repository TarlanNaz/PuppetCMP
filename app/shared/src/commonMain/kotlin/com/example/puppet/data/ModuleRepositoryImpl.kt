package com.example.puppet.data

import com.example.puppet.domain.ModuleRepository
import com.example.puppet.domain.PuppetModule

class ModuleRepositoryImpl : ModuleRepository {
    override suspend fun getModules(): List<PuppetModule> = mockModules

    override suspend fun getModule(slug: String): PuppetModule? =
        mockModules.find { it.slug == slug }
}
