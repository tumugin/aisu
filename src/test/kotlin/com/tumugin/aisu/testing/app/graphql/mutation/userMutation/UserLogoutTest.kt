package com.tumugin.aisu.testing.app.graphql.mutation.userMutation

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UserLogout
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserLogoutTest : BaseKtorTest() {
  @Test
  fun testUserLogout() = testAisuApplication {
    val loggedUserAndCookieValue = seedUserAndLoginAndGetCookieValue(this)
    val client = createClient {
      install(HttpCookies)
    }
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserLogout()) {
      header("Cookie", loggedUserAndCookieValue.cookieValue)
    }
    val cookies = client.cookies("http://localhost/")
    Assertions.assertNull(result.errors)
    Assertions.assertNull(cookies.firstOrNull { c -> c.name == "USER_AUTH" })
  }
}
