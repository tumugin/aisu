package com.tumugin.aisu.testing.app.graphql.mutation.chekiMutation

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UpdateCheki
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateChekiParamsInput
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UpdateChekiTest : BaseKtorTest() {
  @Test
  fun testUpdateCheki() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val cheki = ChekiSeeder().seedChekiWithAttachments(seededUserAndLoginInfo.user.userId)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      UpdateCheki(
        UpdateCheki.Variables(
          cheki.chekiId.value.toString(),
          AddOrUpdateChekiParamsInput(
            100,
            "2022-12-07T15:30:00+09:00",
            "1",
            "1"
          )
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }
    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.cheki?.updateCheki)
  }
}
