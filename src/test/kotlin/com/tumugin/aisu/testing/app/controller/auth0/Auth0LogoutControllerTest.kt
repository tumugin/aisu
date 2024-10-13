package com.tumugin.aisu.testing.app.controller.auth0

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Auth0LogoutControllerTest : BaseKtorTest() {
  @Test
  fun testLogout(): Unit = testAisuApplication {
    val cookieValue = loginAndGetCookieValue(this, UserSeeder().seedUser())
    client.post("/auth0/logout") {
      header("Cookie", cookieValue)
      addCSRFTokenHeader(this)
    }.apply {
      assertEquals(HttpStatusCode.Found, status)
      assertEquals(
        "https://example.com/v2/logout?returnTo=http%3A%2F%2Flocalhost%3A8080%2F&client_id=test",
        headers["Location"]
      )
      assertEquals(
        "USER_AUTH=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; HttpOnly; SameSite=lax; \$x-enc=URI_ENCODING",
        headers["Set-Cookie"]
      )
    }
  }
}
