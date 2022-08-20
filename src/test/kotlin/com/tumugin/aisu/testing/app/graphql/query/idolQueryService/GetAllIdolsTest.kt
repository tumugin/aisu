package com.tumugin.aisu.testing.app.graphql.query.idolQueryService

import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetAllIdolsQuery
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetAllIdolsTest : BaseKtorTest() {
  @Test
  fun testGetAllIdols() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.PUBLIC_ACTIVE)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetAllIdolsQuery(GetAllIdolsQuery.Variables(1))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.getAllIdols?.idols)
    Assertions.assertEquals(1, result.data?.getAllIdols?.idols?.size)
    Assertions.assertEquals(1, result.data?.getAllIdols?.currentPage)
    Assertions.assertEquals(1, result.data?.getAllIdols?.pageCount)
  }
}
