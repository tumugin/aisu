package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.BaseKtorTest
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginControllerTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @BeforeTest
  fun seedUser() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testLogin() = testAisuApplication {
    client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest("aoisuzu@example.com", "aoisuzu")))
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      assertTrue(headers["Set-Cookie"]!!.contains("USER_AUTH"))
    }
  }

  @Test
  fun testLoginFailed() = testAisuApplication {
    client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest("mayfujimiya@example.com", "maypomu")))
    }.apply {
      assertEquals(HttpStatusCode.Forbidden, status)
    }
  }
}
