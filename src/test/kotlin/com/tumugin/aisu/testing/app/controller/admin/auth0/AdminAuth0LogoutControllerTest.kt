package com.tumugin.aisu.testing.app.controller.admin.auth0

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdminAuth0LogoutControllerTest : BaseKtorTest() {
  @Test
  fun testLogout(): Unit = testAisuApplication {
    val cookieValue = adminLoginAndGetCookieValue(this, AdminUserSeeder().seedAdminUser())
    client.post("/admin/auth0/logout") {
      header("Cookie", cookieValue)
      addCSRFTokenHeader(this)
    }.apply {
      Assertions.assertEquals(HttpStatusCode.Found, status)
      Assertions.assertEquals(
        "https://example.com/v2/logout?returnTo=http%3A%2F%2Flocalhost%3A8080%2F&client_id=test",
        headers["Location"]
      )
      Assertions.assertEquals(
        "ADMIN_USER_AUTH=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; HttpOnly; SameSite=lax; \$x-enc=URI_ENCODING",
        headers["Set-Cookie"]
      )
    }
  }
}
