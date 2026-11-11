import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

// Адрес источника данных в репозитории не хранится: он приходит снаружи
// (-Pforge.baseUrl=…, переменная окружения FORGE_BASE_URL или local.properties)
// и попадает в код только в виде сгенерированного при сборке AppConfig.
abstract class GenerateAppConfig : DefaultTask() {
    @get:Input
    @get:Optional
    abstract val forgeBaseUrl: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val url = forgeBaseUrl.orNull?.trim()?.trimEnd('/')
        if (url.isNullOrEmpty()) {
            throw GradleException(
                "Не задан адрес источника данных. Добавьте строку forge.baseUrl=<адрес API> " +
                    "в local.properties, либо передайте -Pforge.baseUrl=…, " +
                    "либо задайте переменную окружения FORGE_BASE_URL (см. README).",
            )
        }
        if (!Regex("^https?://[A-Za-z0-9._~:/-]+$").matches(url)) {
            throw GradleException("forge.baseUrl не похож на адрес http(s): $url")
        }
        val file = outputDir.get().asFile.resolve("com/example/puppet/config/AppConfig.kt")
        file.parentFile.mkdirs()
        file.writeText(
            "package com.example.puppet.config\n\n" +
                "internal object AppConfig {\n" +
                "    const val FORGE_BASE_URL: String = \"" + url + "\"\n" +
                "}\n",
        )
    }
}

val localForgeBaseUrl = providers
    .fileContents(rootProject.layout.projectDirectory.file("local.properties"))
    .asText
    .map { text -> Properties().apply { load(text.reader()) }.getProperty("forge.baseUrl").orEmpty() }
    .filter { it.isNotBlank() }

val generateAppConfig = tasks.register<GenerateAppConfig>("generateAppConfig") {
    forgeBaseUrl.set(
        providers.gradleProperty("forge.baseUrl")
            .orElse(providers.environmentVariable("FORGE_BASE_URL"))
            .orElse(localForgeBaseUrl),
    )
    outputDir.set(layout.buildDirectory.dir("generated/appConfig/kotlin"))
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(generateAppConfig)
}
// Чтобы AppConfig был виден в IDE сразу после синхронизации проекта.
tasks.named { it == "prepareKotlinIdeaImport" }.configureEach {
    dependsOn(generateAppConfig)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    android {
       namespace = "com.example.puppet.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        commonMain {
            kotlin.srcDir(generateAppConfig.flatMap { it.outputDir })
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.clientOkhttp)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.clientOkhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.clientDarwin)
        }
        jsMain.dependencies {
            implementation(libs.ktor.clientJs)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.clientJs)
        }
        commonMain.dependencies {
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.ktor.clientCore)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutinesTest)
            implementation(libs.ktor.clientMock)
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.puppet.resources"
}
