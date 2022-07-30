package com.tumugin.aisu.testing.app.graphql.query

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.CurrentUser
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserQueryServiceTest : BaseKtorTest() {
  @Test
  fun testGetCurrentUserWithNotLoggedIn() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(CurrentUser())
    Assertions.assertNull(result.data?.currentUser)
    Assertions.assertNull(result.errors)
  }

  @Test
  fun testGetCurrentUser() = testAisuApplication {
    val loggedUserAndCookieValue = seedUserAndLoginAndGetCookieValue(this@testAisuApplication)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(CurrentUser()) {
      header("Cookie", loggedUserAndCookieValue.cookieValue)
    }
    Assertions.assertNotNull(result.data?.currentUser)
    Assertions.assertNull(result.errors)
  }
}
