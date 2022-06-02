package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.BaseKtorTest
import com.tumugin.aisu.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class LogoutControllerTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @BeforeTest
  fun seedUser() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testLogout() = testAisuApplication {
    client.post("/api/logout") {
      cookie("USER_AUTH", "test")
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      assertEquals(
        "USER_AUTH=; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; \$x-enc=URI_ENCODING",
        headers["Set-Cookie"]
      )
    }
  }
}
