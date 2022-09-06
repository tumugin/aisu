package com.tumugin.aisu.testing.app.graphql.query.regulationQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetRegulation
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetRegulationTest : BaseKtorTest() {
  @Test
  fun testGetRegulationTest() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    val group = GroupSeeder().seedGroup(user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetRegulation(GetRegulation.Variables(regulation.regulationId.value.toString()))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.getRegulation)
  }
}
