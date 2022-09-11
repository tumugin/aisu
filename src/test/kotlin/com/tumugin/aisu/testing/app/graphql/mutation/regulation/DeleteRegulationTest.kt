package com.tumugin.aisu.testing.app.graphql.mutation.regulation

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.DeleteRegulation
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteRegulationTest : BaseKtorTest() {
  lateinit var user: User
  lateinit var cookieValue: String

  @BeforeEach
  fun beforeEach() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    user = seededUserAndLoginInfo.user
    cookieValue = seededUserAndLoginInfo.cookieValue
  }

  @Test
  fun testDeleteRegulation() = testAisuApplication {
    val group = GroupSeeder().seedGroup(user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      DeleteRegulation(
        DeleteRegulation.Variables(
          regulation.regulationId.value.toString()
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.regulation?.deleteRegulation)
  }
}
