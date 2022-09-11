package com.tumugin.aisu.testing.app.graphql.mutation.regulation

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddRegulation
import com.tumugin.aisu.testing.graphql.client.enums.RegulationStatus
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateRegulationParamsInput
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddRegulationTest : BaseKtorTest() {
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
  fun testAddRegulation() = testAisuApplication {
    val group = GroupSeeder().seedGroup(user.userId)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddRegulation(
        AddRegulation.Variables(
          AddOrUpdateRegulationParamsInput(
            group.groupId.value.toString(), "てすと", "2ショチェキ(サインなし)", RegulationStatus.ACTIVE, 2000
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.regulation?.addRegulation)
  }
}
