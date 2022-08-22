package com.tumugin.aisu.testing.app.graphql.mutation.idolMutationService

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.DeleteIdolMutation
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteIdolTest : BaseKtorTest() {
  lateinit var user: User
  lateinit var cookieValue: String
  lateinit var idol: Idol

  @BeforeEach
  fun beforeEach() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    user = seededUserAndLoginInfo.user
    cookieValue = seededUserAndLoginInfo.cookieValue
    idol = IdolSeeder().seedIdol(user.userId)
  }

  @Test
  fun testDeleteIdol() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      DeleteIdolMutation(
        DeleteIdolMutation.Variables(
          idol.idolId.value.toString()
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.idol)
  }
}
