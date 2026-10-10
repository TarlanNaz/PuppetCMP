package com.example.crypto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PuppetModuleResponse(
    val pagination: Pagination,
    val results: List<PuppetModule>
)

@Serializable
data class Pagination(
    val limit: Int,
    val offset: Int,
    val first: String,
    val previous: String?,
    val current: String,
    val next: String?,
    val total: Int
)

@Serializable
data class PuppetModule(
    val uri: String,
    val slug: String,
    val name: String,
    val downloads: Long,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val owner: Owner,
    @SerialName("current_release") val currentRelease: CurrentRelease
)

@Serializable
data class Owner(
    val uri: String,
    val slug: String,
    val username: String,
    @SerialName("gravatar_id") val gravatarId: String
)

@Serializable
data class CurrentRelease(
    val uri: String,
    val slug: String,
    val version: String,
    val metadata: Metadata,
    val tags: List<String>,
    val pdk: Boolean,
    @SerialName("validation_score") val validationScore: Int,
    @SerialName("file_size") val fileSize: Long,
    @SerialName("file_md5") val fileMd5: String,
    val downloads: Long
)

@Serializable
data class Metadata(
    val name: String,
    val version: String,
    val author: String,
    val summary: String,
    val license: String,
    val source: String
)

val mockModules = listOf(
    PuppetModule(
        uri = "/v3/modules/puppetlabs-stdlib",
        slug = "puppetlabs-stdlib",
        name = "stdlib",
        downloads = 83958913,
        createdAt = "2011-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-stdlib-9.4.1",
            slug = "puppetlabs-stdlib-9.4.1",
            version = "9.4.1",
            metadata = Metadata(
                name = "puppetlabs-stdlib",
                version = "9.4.1",
                author = "Puppet",
                summary = "Standard library of useful types, functions and resource types",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-stdlib"
            ),
            tags = listOf("puppet", "stdlib", "functions"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "ed64593ed303bc374a4cf067b8cb26e9",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-concat",
        slug = "puppetlabs-concat",
        name = "concat",
        downloads = 53958913,
        createdAt = "2012-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-concat-9.0.0",
            slug = "puppetlabs-concat-9.0.0",
            version = "9.0.0",
            metadata = Metadata(
                name = "puppetlabs-concat",
                version = "9.0.0",
                author = "Puppet",
                summary = "Construct files from multiple fragments.",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-concat"
            ),
            tags = listOf("puppet", "concat"),
            pdk = true,
            validationScore = 100,
            fileSize = 157529,
            fileMd5 = "12345",
            downloads = 134123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-postgresql",
        slug = "puppetlabs-postgresql",
        name = "postgresql",
        downloads = 43958913,
        createdAt = "2013-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-postgresql-10.0.1",
            slug = "puppetlabs-postgresql-10.0.1",
            version = "10.0.1",
            metadata = Metadata(
                name = "puppetlabs-postgresql",
                version = "10.0.1",
                author = "Puppet",
                summary = "Installs, configures, and manages PostgreSQL",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-postgresql"
            ),
            tags = listOf("puppet", "postgresql"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-mysql",
        slug = "puppetlabs-mysql",
        name = "mysql",
        downloads = 33958913,
        createdAt = "2014-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-mysql-15.0.0",
            slug = "puppetlabs-mysql-15.0.0",
            version = "15.0.0",
            metadata = Metadata(
                name = "puppetlabs-mysql",
                version = "15.0.0",
                author = "Puppet",
                summary = "Installs, configures, and manages MySQL",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-mysql"
            ),
            tags = listOf("puppet", "mysql"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-apache",
        slug = "puppetlabs-apache",
        name = "apache",
        downloads = 23958913,
        createdAt = "2015-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-apache-12.0.0",
            slug = "puppetlabs-apache-12.0.0",
            version = "12.0.0",
            metadata = Metadata(
                name = "puppetlabs-apache",
                version = "12.0.0",
                author = "Puppet",
                summary = "Installs, configures, and manages Apache web server",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-apache"
            ),
            tags = listOf("puppet", "apache"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-firewall",
        slug = "puppetlabs-firewall",
        name = "firewall",
        downloads = 13958913,
        createdAt = "2016-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-firewall-7.0.0",
            slug = "puppetlabs-firewall-7.0.0",
            version = "7.0.0",
            metadata = Metadata(
                name = "puppetlabs-firewall",
                version = "7.0.0",
                author = "Puppet",
                summary = "Manages Firewalls such as iptables",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-firewall"
            ),
            tags = listOf("puppet", "firewall"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456789",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-ntp",
        slug = "puppetlabs-ntp",
        name = "ntp",
        downloads = 9958913,
        createdAt = "2017-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-ntp-9.1.0",
            slug = "puppetlabs-ntp-9.1.0",
            version = "9.1.0",
            metadata = Metadata(
                name = "puppetlabs-ntp",
                version = "9.1.0",
                author = "Puppet",
                summary = "Installs, configures, and manages the NTP service",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-ntp"
            ),
            tags = listOf("puppet", "ntp"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567890",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-apt",
        slug = "puppetlabs-apt",
        name = "apt",
        downloads = 8958913,
        createdAt = "2018-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-apt-9.3.0",
            slug = "puppetlabs-apt-9.3.0",
            version = "9.3.0",
            metadata = Metadata(
                name = "puppetlabs-apt",
                version = "9.3.0",
                author = "Puppet",
                summary = "Provides helpful definitions for dealing with Apt",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-apt"
            ),
            tags = listOf("puppet", "apt"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678901",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-docker",
        slug = "puppetlabs-docker",
        name = "docker",
        downloads = 7958913,
        createdAt = "2019-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-docker-8.1.0",
            slug = "puppetlabs-docker-8.1.0",
            version = "8.1.0",
            metadata = Metadata(
                name = "puppetlabs-docker",
                version = "8.1.0",
                author = "Puppet",
                summary = "Installs, configures, and manages Docker",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-docker"
            ),
            tags = listOf("puppet", "docker"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456789012",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-haproxy",
        slug = "puppetlabs-haproxy",
        name = "haproxy",
        downloads = 6958913,
        createdAt = "2020-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-haproxy-6.2.0",
            slug = "puppetlabs-haproxy-6.2.0",
            version = "6.2.0",
            metadata = Metadata(
                name = "puppetlabs-haproxy",
                version = "6.2.0",
                author = "Puppet",
                summary = "Installs, configures, and manages HAProxy",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-haproxy"
            ),
            tags = listOf("puppet", "haproxy"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567890123",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-nginx",
        slug = "puppetlabs-nginx",
        name = "nginx",
        downloads = 5958913,
        createdAt = "2021-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-nginx-5.1.0",
            slug = "puppetlabs-nginx-5.1.0",
            version = "5.1.0",
            metadata = Metadata(
                name = "puppetlabs-nginx",
                version = "5.1.0",
                author = "Puppet",
                summary = "Installs, configures, and manages Nginx",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-nginx"
            ),
            tags = listOf("puppet", "nginx"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678901234",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-kubernetes",
        slug = "puppetlabs-kubernetes",
        name = "kubernetes",
        downloads = 4958913,
        createdAt = "2022-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-kubernetes-8.0.0",
            slug = "puppetlabs-kubernetes-8.0.0",
            version = "8.0.0",
            metadata = Metadata(
                name = "puppetlabs-kubernetes",
                version = "8.0.0",
                author = "Puppet",
                summary = "Installs, configures, and manages Kubernetes",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-kubernetes"
            ),
            tags = listOf("puppet", "kubernetes"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456789012345",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-java",
        slug = "puppetlabs-java",
        name = "java",
        downloads = 3958913,
        createdAt = "2013-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-java-8.2.0",
            slug = "puppetlabs-java-8.2.0",
            version = "8.2.0",
            metadata = Metadata(
                name = "puppetlabs-java",
                version = "8.2.0",
                author = "Puppet",
                summary = "Installs Java",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-java"
            ),
            tags = listOf("puppet", "java"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567890123456",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-tomcat",
        slug = "puppetlabs-tomcat",
        name = "tomcat",
        downloads = 2958913,
        createdAt = "2014-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-tomcat-6.1.0",
            slug = "puppetlabs-tomcat-6.1.0",
            version = "6.1.0",
            metadata = Metadata(
                name = "puppetlabs-tomcat",
                version = "6.1.0",
                author = "Puppet",
                summary = "Installs Tomcat",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-tomcat"
            ),
            tags = listOf("puppet", "tomcat"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678901234567",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-rabbitmq",
        slug = "puppetlabs-rabbitmq",
        name = "rabbitmq",
        downloads = 1958913,
        createdAt = "2015-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-rabbitmq-14.0.0",
            slug = "puppetlabs-rabbitmq-14.0.0",
            version = "14.0.0",
            metadata = Metadata(
                name = "puppetlabs-rabbitmq",
                version = "14.0.0",
                author = "Puppet",
                summary = "Installs, configures, and manages RabbitMQ",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-rabbitmq"
            ),
            tags = listOf("puppet", "rabbitmq"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456789012345678",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-vcsrepo",
        slug = "puppetlabs-vcsrepo",
        name = "vcsrepo",
        downloads = 958913,
        createdAt = "2016-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-vcsrepo-6.0.0",
            slug = "puppetlabs-vcsrepo-6.0.0",
            version = "6.0.0",
            metadata = Metadata(
                name = "puppetlabs-vcsrepo",
                version = "6.0.0",
                author = "Puppet",
                summary = "Provides a type to manage version control repositories",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-vcsrepo"
            ),
            tags = listOf("puppet", "vcsrepo"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567890123456789",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-inifile",
        slug = "puppetlabs-inifile",
        name = "inifile",
        downloads = 858913,
        createdAt = "2017-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-inifile-6.1.0",
            slug = "puppetlabs-inifile-6.1.0",
            version = "6.1.0",
            metadata = Metadata(
                name = "puppetlabs-inifile",
                version = "6.1.0",
                author = "Puppet",
                summary = "Resource types for managing settings in INI files",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-inifile"
            ),
            tags = listOf("puppet", "inifile"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678901234567890",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-hocon",
        slug = "puppetlabs-hocon",
        name = "hocon",
        downloads = 758913,
        createdAt = "2018-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-hocon-1.2.0",
            slug = "puppetlabs-hocon-1.2.0",
            version = "1.2.0",
            metadata = Metadata(
                name = "puppetlabs-hocon",
                version = "1.2.0",
                author = "Puppet",
                summary = "Resource types for managing settings in HOCON files",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-hocon"
            ),
            tags = listOf("puppet", "hocon"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "123456789012345678901",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-registry",
        slug = "puppetlabs-registry",
        name = "registry",
        downloads = 658913,
        createdAt = "2019-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-registry-5.0.0",
            slug = "puppetlabs-registry-5.0.0",
            version = "5.0.0",
            metadata = Metadata(
                name = "puppetlabs-registry",
                version = "5.0.0",
                author = "Puppet",
                summary = "Native types for managing Windows registry keys and values",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-registry"
            ),
            tags = listOf("puppet", "registry"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "1234567890123456789012",
            downloads = 234123
        )
    ),
    PuppetModule(
        uri = "/v3/modules/puppetlabs-powershell",
        slug = "puppetlabs-powershell",
        name = "powershell",
        downloads = 558913,
        createdAt = "2020-06-21T16:26:15-07:00",
        updatedAt = "2023-08-30T10:11:12Z",
        owner = Owner("/v3/users/puppetlabs", "puppetlabs", "puppetlabs", ""),
        currentRelease = CurrentRelease(
            uri = "/v3/releases/puppetlabs-powershell-6.0.0",
            slug = "puppetlabs-powershell-6.0.0",
            version = "6.0.0",
            metadata = Metadata(
                name = "puppetlabs-powershell",
                version = "6.0.0",
                author = "Puppet",
                summary = "Execute PowerShell cmdlets and scripts",
                license = "Apache-2.0",
                source = "https://github.com/puppetlabs/puppetlabs-powershell"
            ),
            tags = listOf("puppet", "powershell"),
            pdk = true,
            validationScore = 100,
            fileSize = 257529,
            fileMd5 = "12345678901234567890123",
            downloads = 234123
        )
    )
)