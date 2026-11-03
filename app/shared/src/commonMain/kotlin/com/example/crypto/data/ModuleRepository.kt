package com.example.crypto.data

interface ModuleRepository {
    fun getModules(): List<PuppetModule>
    fun getModule(slug: String): PuppetModule?
    fun getModulesByOwner(username: String): List<PuppetModule>
}

class MockModuleRepository : ModuleRepository {
    override fun getModules(): List<PuppetModule> = mockModules

    override fun getModule(slug: String): PuppetModule? =
        mockModules.find { it.slug == slug }

    override fun getModulesByOwner(username: String): List<PuppetModule> =
        mockModules.filter { it.owner.username == username }
}
