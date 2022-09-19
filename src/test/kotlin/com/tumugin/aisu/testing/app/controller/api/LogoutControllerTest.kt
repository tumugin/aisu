package com.tumugin.aisu.testing.app.controller.api

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutControllerTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()
  private lateinit var user: User

  @BeforeEach
  fun seedUser() = runTest {
    user = userSeeder.seedUser()
  }

  @Test
  fun testLogout(): Unit = testAisuApplication {
    val cookieValue = loginAndGetCookieValue(this, user)
    client.post("/api/logout") {
      header("Cookie", cookieValue)
      addCSRFTokenHeader(this)
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      assertEquals(
        "USER_AUTH=; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; HttpOnly; SameSite=lax; \$x-enc=URI_ENCODING",
        headers["Set-Cookie"]
      )
    }
  }
}
