package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.BaseKtorTest
import com.tumugin.aisu.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogoutController : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @BeforeTest
  fun seedUser() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testLogout() = testAisuApplication {
    client.post("/api/logout") {
    }.apply {
      assertEquals(HttpStatusCode.OK, status)
      assertTrue(headers["Set-Cookie"]!!.contains("USER_AUTH"))
    }
  }
}
