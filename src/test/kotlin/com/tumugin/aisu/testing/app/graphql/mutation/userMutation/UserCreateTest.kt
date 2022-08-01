package com.tumugin.aisu.testing.app.graphql.mutation.userMutation

import com.tumugin.aisu.app.graphql.GraphQLErrorTypes
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UserCreate
import com.tumugin.aisu.testing.seeder.UserSeeder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserCreateTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()

  @Test
  fun testUserCreate() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserCreate())
    Assertions.assertNotNull(result.data?.user?.userCreate)
    Assertions.assertNull(result.errors)
  }

  @Test
  fun testUserCreateWithDuplicateUser() = testAisuApplication {
    userSeeder.seedUser(userEmail = UserEmail("aoisuzu@example.com"))
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(UserCreate())
    Assertions.assertNull(result.data?.user?.userCreate)
    Assertions.assertNotNull(result.errors)
    Assertions.assertEquals(GraphQLErrorTypes.UserAlreadyExists.name, result.errors?.first()?.extensions?.get("type"))
  }
}
