package com.tumugin.aisu.testing.app.controller.api

import com.tumugin.aisu.app.responder.ResponseStatusType
import com.tumugin.aisu.app.responder.api.MetadataResponder
import com.tumugin.aisu.testing.BaseKtorTest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MetadataControllerTest : BaseKtorTest() {
  @Test
  fun get() = testAisuApplication {
    val userAndCookie = seedUserAndLoginAndGetCookieValue(this)
    client.get("/api/metadata") {
      contentType(ContentType.Application.Json)
      header("Cookie", userAndCookie.cookieValue)
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      val metadata = Json.decodeFromString(MetadataResponder.serializer(), bodyAsText())
      assertEquals(ResponseStatusType.SUCCESS, metadata.status)
      assertTrue(metadata.value.csrfToken.length > 10)
      assertNotNull(metadata.value.user)
    }
  }
}
