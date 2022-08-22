package com.tumugin.aisu.testing.app.graphql.mutation.idolMutationService

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddIdolMutation
import com.tumugin.aisu.testing.graphql.client.enums.IdolStatus
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateIdolParamsInput
import com.tumugin.aisu.testing.seeder.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddIdolTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()
  lateinit var user: User
  lateinit var cookieValue: String

  @BeforeEach
  fun beforeEach() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    user = seededUserAndLoginInfo.user
    cookieValue = seededUserAndLoginInfo.cookieValue
  }

  @Test
  fun testAddIdol() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddIdolMutation(
        AddIdolMutation.Variables(
          AddOrUpdateIdolParamsInput(
            "藍井すず", IdolStatus.PUBLIC_ACTIVE
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.idol)
  }
}
