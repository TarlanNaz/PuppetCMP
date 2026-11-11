package com.example.puppet.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ForgeParserTest {

    @Test
    fun pageIsParsedAndUnknownFieldsAreIgnored() {
        val body = """
            {
              "pagination": {"limit": 20, "offset": 40, "next": "/v3/modules?limit=20&offset=60", "total": 7000, "brand_new": true},
              "results": [
                {
                  "uri": "/v3/modules/puppetlabs-stdlib",
                  "slug": "puppetlabs-stdlib",
                  "name": "stdlib",
                  "downloads": 83958913,
                  "updated_at": "2023-08-30 03:11:12 -0700",
                  "field_from_next_release": {"anything": [1, 2, 3]},
                  "owner": {"uri": "/v3/users/puppetlabs", "username": "puppetlabs", "gravatar_id": "x"},
                  "current_release": {
                    "version": "9.4.1",
                    "tags": ["stdlib", "functions"],
                    "readme": "# long text",
                    "metadata": {"summary": "Standard library", "license": "Apache-2.0", "dependencies": []}
                  }
                }
              ]
            }
        """.trimIndent()

        val page = ForgeParser.parsePage(body)

        assertEquals(60, page.nextOffset)
        val module = page.items.single()
        assertEquals("puppetlabs-stdlib", module.slug)
        assertEquals("puppetlabs", module.owner)
        assertEquals("9.4.1", module.version)
        assertEquals(83958913L, module.downloads)
        assertEquals("Standard library", module.summary)
        assertEquals("Apache-2.0", module.license)
        assertEquals(listOf("stdlib", "functions"), module.tags)
    }

    @Test
    fun lastPageHasNoNextOffset() {
        val body = """{"pagination": {"limit": 20, "offset": 0, "next": null}, "results": []}"""

        assertNull(ForgeParser.parsePage(body).nextOffset)
    }

    @Test
    fun nullsAndOddMetadataDoNotBreakParsing() {
        val body = """
            {
              "slug": "someone-thing",
              "name": "thing",
              "downloads": null,
              "updated_at": null,
              "owner": null,
              "current_release": {"version": null, "tags": null, "metadata": {"summary": null, "license": ["MIT", "GPL"]}}
            }
        """.trimIndent()

        val module = ForgeParser.parseModule(body)

        assertEquals("someone", module.owner)
        assertEquals(0L, module.downloads)
        assertEquals("", module.version)
        assertEquals("", module.summary)
        assertEquals("", module.license)
        assertEquals(emptyList<String>(), module.tags)
    }
}
