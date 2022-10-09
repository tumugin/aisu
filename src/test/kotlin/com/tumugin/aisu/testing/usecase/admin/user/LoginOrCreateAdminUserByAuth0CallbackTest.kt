package com.tumugin.aisu.testing.usecase.admin.user

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.adminUser.AdminUserEmail
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import com.tumugin.aisu.usecase.admin.adminUser.LoginOrCreateAdminUserByAuth0Callback
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.mock.declare

class LoginOrCreateAdminUserByAuth0CallbackTest : BaseKtorTest() {
  @Serializable
  private data class MockUserInfo(
    val sub: String = "12345|test",
    val name: String = "Aoi Suzu",
    val email: String = "suzu@example.com"
  )

  private fun mockAuth0API(): MockEngine {
    val mockEngine = MockEngine { request ->
      val json = Json { encodeDefaults = true }
      respond(
        content = ByteReadChannel(json.encodeToString(MockUserInfo())),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    return mockEngine
  }

  @Test
  fun testWithNonExisting() {
    declare {
      AisuHTTPClient(mockAuth0API())
    }
    testAisuApplication {
      val adminUser = LoginOrCreateAdminUserByAuth0Callback().getOrCreateAdminUserByPrincipal(
        OAuthAccessTokenResponse.OAuth2(
          accessToken = "access_token",
          tokenType = "Bearer",
          expiresIn = 3600,
          refreshToken = "refresh_token"
        )
      )
      Assertions.assertEquals("Aoi Suzu", adminUser.adminUserName.value)
      Assertions.assertEquals(1L, adminUser.adminUserId.value)
      Assertions.assertEquals("suzu@example.com", adminUser.adminUserEmail.value)
    }
  }

  @Test
  fun testWithExisting() = runTest {
    declare {
      AisuHTTPClient(mockAuth0API())
    }
    AdminUserSeeder().seedAdminUser(adminUserEmail = AdminUserEmail("suzu@example.com"))
    testAisuApplication {
      val adminUser = LoginOrCreateAdminUserByAuth0Callback().getOrCreateAdminUserByPrincipal(
        OAuthAccessTokenResponse.OAuth2(
          accessToken = "access_token",
          tokenType = "Bearer",
          expiresIn = 3600,
          refreshToken = "refresh_token"
        )
      )
      Assertions.assertEquals("藍井すず", adminUser.adminUserName.value)
      Assertions.assertEquals(1L, adminUser.adminUserId.value)
      Assertions.assertEquals("suzu@example.com", adminUser.adminUserEmail.value)
    }
  }
}
