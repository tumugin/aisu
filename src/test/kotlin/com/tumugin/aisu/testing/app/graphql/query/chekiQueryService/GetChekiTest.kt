package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetCheki
import com.tumugin.aisu.testing.seeder.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetChekiTest : BaseKtorTest() {
  @Test
  fun testGetCheki() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    val idol = IdolSeeder().seedIdol(user.userId)
    val group = GroupSeeder().seedGroup(user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    val cheki = ChekiSeeder().seedCheki(user.userId, idol.idolId, regulation.regulationId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(GetCheki()) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }
    Assertions.assertNotNull(result.data?.getCheki)
    Assertions.assertNull(result.errors)
  }
}
