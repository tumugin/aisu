package com.tumugin.aisu.testing.app.graphql.mutation.userMutation

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UserLogout
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserLogoutTest : BaseKtorTest() {
  @Test
  fun testUserLogout() = testAisuApplication {
    val loggedUserAndCookieValue = seedUserAndLoginAndGetCookieValue(this)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserLogout()) {
      header("Cookie", loggedUserAndCookieValue.cookieValue)
    }
    Assertions.assertNull(result.errors)
  }
}
