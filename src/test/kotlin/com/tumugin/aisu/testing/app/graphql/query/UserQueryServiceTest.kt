package com.tumugin.aisu.testing.app.graphql.query

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.CurrentUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URL

class UserQueryServiceTest : BaseKtorTest() {

  @Test
  fun testGetCurrentUserWithNotLoggedIn() = testAisuApplication {
    val graphQLClient = GraphQLKtorClient(URL("http://localhost/graphql"), client)
    val result = graphQLClient.execute(CurrentUser())
    Assertions.assertNull(result.data?.currentUser)
    Assertions.assertNull(result.errors)
  }
}
