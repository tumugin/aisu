package com.tumugin.aisu.testing.app.controller.api

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginControllerTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @BeforeEach
  fun seedUser() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testLogin(): Unit = testAisuApplication {
    client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest("aoisuzu@example.com", "aoisuzu")))
      addCSRFTokenHeader(this)
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      assertTrue(headers["Set-Cookie"]!!.contains("USER_AUTH"))
    }
  }

  @Test
  fun testLoginFailed(): Unit = testAisuApplication {
    client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest("mayfujimiya@example.com", "maypomu")))
      addCSRFTokenHeader(this)
    }.apply {
      assertEquals(HttpStatusCode.Forbidden, status)
    }
  }
}
