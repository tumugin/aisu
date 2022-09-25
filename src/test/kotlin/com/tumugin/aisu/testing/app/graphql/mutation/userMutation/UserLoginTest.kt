package com.tumugin.aisu.testing.app.graphql.mutation.userMutation

import com.tumugin.aisu.app.graphql.GraphQLErrorTypes
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UserLogin
import com.tumugin.aisu.testing.graphql.client.UserLoginWithWrongID
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.plugins.cookies.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserLoginTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @BeforeEach
  fun seedUser() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testUserLogin() = testAisuApplication {
    val client = createClient {
      install(HttpCookies)
    }
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserLogin())
    val cookies = client.cookies("http://localhost/")
    Assertions.assertNotNull(result.data?.user?.userLogin)
    Assertions.assertNull(result.errors)
    Assertions.assertTrue(cookies.first { c -> c.name == "USER_AUTH" }.value.length > 50)
  }

  @Test
  fun testUserWithWrongIDLogin() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserLoginWithWrongID())
    Assertions.assertNull(result.data?.user?.userLogin)
    Assertions.assertNotNull(result.errors)
    Assertions.assertEquals(GraphQLErrorTypes.LoginFailed.name, result.errors?.first()?.extensions?.get("type"))
  }
}
