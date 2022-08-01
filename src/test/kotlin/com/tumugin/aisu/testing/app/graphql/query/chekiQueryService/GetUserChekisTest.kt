package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.LoggedUserAndCookieValue
import com.tumugin.aisu.testing.graphql.client.GetUserChekis
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetUserChekisTest : BaseKtorTest() {
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
  fun testGetUserChekis() = testAisuApplication {
    val preparedResult = prepare()

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetUserChekis(
        GetUserChekis.Variables(
          "2021-12-01T00:00:00+09:00",
          "2021-12-31T23:59:59+09:00"
        )
      )
    ) {
      header("Cookie", preparedResult.loggedUserAndCookieValue.cookieValue)
    }

    Assertions.assertNotNull(result.data?.currentUserChekis?.getUserChekis)
    Assertions.assertEquals(2, result.data?.currentUserChekis?.getUserChekis?.size)
    Assertions.assertNull(result.errors)
  }

  @Test
  fun testGetUserChekisWithIdolId() = testAisuApplication {
    val preparedResult = prepare()

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetUserChekis(
        GetUserChekis.Variables(
          "2021-12-01T00:00:00+09:00",
          "2021-12-31T23:59:59+09:00",
          preparedResult.idol.idolId.value.toString()
        )
      )
    ) {
      header("Cookie", preparedResult.loggedUserAndCookieValue.cookieValue)
    }

    Assertions.assertNotNull(result.data?.currentUserChekis?.getUserChekis)
    Assertions.assertEquals(1, result.data?.currentUserChekis?.getUserChekis?.size)
    Assertions.assertNull(result.errors)
  }
}
