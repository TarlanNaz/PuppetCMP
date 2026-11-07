package com.example.crypto.data

interface ModuleRepository {
    fun getModules(): List<PuppetModule>
    fun getModule(slug: String): PuppetModule?
    fun getRelatedModules(module: PuppetModule): List<PuppetModule>
}

class MockModuleRepository : ModuleRepository {
    override fun getModules(): List<PuppetModule> = mockModules

    override fun getModule(slug: String): PuppetModule? =
        mockModules.find { it.slug == slug }

    override fun getRelatedModules(module: PuppetModule): List<PuppetModule> =
        mockModules
            .filter { it.owner.username == module.owner.username && it.slug != module.slug }
            .take(3)
}
