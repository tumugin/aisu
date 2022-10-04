package com.tumugin.aisu.testing.usecase.client.auth0

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.auth0.Auth0UserId
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.Auth0UserSeeder
import com.tumugin.aisu.usecase.client.auth0.LoginOrCreateUserByAuth0Callback
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.mock.declare

class LoginOrCreateUserByAuth0CallbackTest : BaseKtorTest() {
  @Serializable
  private data class MockUserInfo(
    @SerialName("user_id") val userId: String = "12345|test",
    val name: String = "Aoi Suzu",
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
      val user = LoginOrCreateUserByAuth0Callback().getOrCreateUserByPrincipal(
        OAuthAccessTokenResponse.OAuth2(
          accessToken = "access_token", tokenType = "Bearer", expiresIn = 3600, refreshToken = "refresh_token"
        )
      )
      Assertions.assertEquals("Aoi Suzu", user.user.userName.value)
      Assertions.assertEquals("12345|test", user.auth0UserId.value)
    }
  }

  @Test
  fun testWithExisting() = runTest {
    val existingUser = Auth0UserSeeder().seedAuth0User(
      auth0UserId = Auth0UserId("12345|test")
    )
    declare {
      AisuHTTPClient(mockAuth0API())
    }
    testAisuApplication {
      val user = LoginOrCreateUserByAuth0Callback().getOrCreateUserByPrincipal(
        OAuthAccessTokenResponse.OAuth2(
          accessToken = "access_token", tokenType = "Bearer", expiresIn = 3600, refreshToken = "refresh_token"
        )
      )
      Assertions.assertEquals(existingUser.user.userId, user.user.userId)
      Assertions.assertEquals(existingUser.user.userName, user.user.userName)
      Assertions.assertEquals("12345|test", user.auth0UserId.value)
    }
  }
}
