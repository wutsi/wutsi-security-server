package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.dao.KeyRepository
import com.wutsi.platform.security.dto.CreateKeyResponse
import com.wutsi.platform.security.entity.KeyEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/CreateKeyController.sql"])
class CreateKeyControllerTest {
    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var dao: KeyRepository

    @Value("\${wutsi.application.key.algorithm}")
    private lateinit var keyAlgorithm: String

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/keys"
    }

    @Test
    fun invoke() {
        val response = rest.postForEntity(url, emptyMap<String, String>(), CreateKeyResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        // Key is created
        val key = dao.findById(response.body.id).get()
        assertEquals(keyAlgorithm, key.algorithm)
        assertTrue(key.active)
        assertNotNull(key.created)
        assertNull(key.expired)

        // Previous keys are expired
        dao.findAll()
            .filter { it.id != response.body.id }
            .forEach { assertKeyExpired(it) }
    }

    private fun assertKeyExpired(key: KeyEntity) {
        assertFalse(key.active)
        assertNotNull(key.created)
        assertNotNull(key.expired)
    }
}
