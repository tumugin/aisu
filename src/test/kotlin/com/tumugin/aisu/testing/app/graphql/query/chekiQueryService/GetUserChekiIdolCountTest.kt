package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.LoggedUserAndCookieValue
import com.tumugin.aisu.testing.graphql.client.GetUserChekiIdolCount
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetUserChekiIdolCountTest : BaseKtorTest() {
  private data class PrepareResult(val idol: Idol, val loggedUserAndCookieValue: LoggedUserAndCookieValue)

  private fun prepare() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    val idol = IdolSeeder().seedIdol(user.userId)
    val group = GroupSeeder().seedGroup(user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    ChekiSeeder().seedCheki(user.userId, idol.idolId, regulation.regulationId)
    ChekiSeeder().seedCheki(user.userId, IdolSeeder().seedIdol(user.userId).idolId, regulation.regulationId)
    return@testAisuApplication PrepareResult(idol, seededUserAndLoginInfo)
  }

  @Test
  fun testGetUserChekiIdolCount() = testAisuApplication {
    val prepareResult = prepare()
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetUserChekiIdolCount(
        GetUserChekiIdolCount.Variables(
          "2021-01-01T00:00:00+09:00",
          "2021-12-31T23:59:59+09:00"
        )
      )
    ) {
      header("Cookie", prepareResult.loggedUserAndCookieValue.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertEquals(2, result.data?.currentUserChekis?.getUserChekiIdolCount?.size)
  }
}
