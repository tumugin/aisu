package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.LoggedUserAndCookieValue
import com.tumugin.aisu.testing.graphql.client.GetChekiMonthCountByIdol
import com.tumugin.aisu.testing.graphql.client.inputs.GetChekiMonthCountByIdolParamsInput
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetChekiMonthCountByIdolTest : BaseKtorTest() {
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
  fun testGetChekiMonthCountByIdol() = testAisuApplication {
    val prepareResult = prepare()
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetChekiMonthCountByIdol(
        GetChekiMonthCountByIdol.Variables(
          GetChekiMonthCountByIdolParamsInput(
            baseTimezone = "Asia/Tokyo",
            idolId = prepareResult.idol.idolId.value.toString()
          )
        )
      )
    ) {
      header("Cookie", prepareResult.loggedUserAndCookieValue.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertEquals(1, result.data?.currentUserChekis?.getChekiMonthCountByIdol?.size)
    Assertions.assertEquals(1, result.data?.currentUserChekis?.getChekiMonthCountByIdol?.get(0)?.count)
  }
}
