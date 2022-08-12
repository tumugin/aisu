package com.tumugin.aisu.testing.app.graphql.mutation.chekiMutation

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.DeleteCheki
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DeleteChekiTest : BaseKtorTest() {
  @Test
  fun testUpdateCheki() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val cheki = ChekiSeeder().seedChekiWithAttachments(seededUserAndLoginInfo.user.userId)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      DeleteCheki(
        DeleteCheki.Variables(
          cheki.chekiId.value.toString()
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }
    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.cheki?.deleteCheki)
  }
}
