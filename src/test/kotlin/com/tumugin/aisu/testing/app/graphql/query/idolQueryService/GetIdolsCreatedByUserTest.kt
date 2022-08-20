package com.tumugin.aisu.testing.app.graphql.query.idolQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetIdolsCreatedByUser
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetIdolsCreatedByUserTest : BaseKtorTest() {
  @Test
  fun testGetIdolsCreatedByUser() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    IdolSeeder().seedIdol(user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetIdolsCreatedByUser(GetIdolsCreatedByUser.Variables(1))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.currentUserIdols?.getIdolsCreatedByUser?.idols)
    Assertions.assertEquals(1, result.data?.currentUserIdols?.getIdolsCreatedByUser?.idols?.size)
    Assertions.assertEquals(1, result.data?.currentUserIdols?.getIdolsCreatedByUser?.currentPage)
    Assertions.assertEquals(1, result.data?.currentUserIdols?.getIdolsCreatedByUser?.pageCount)
  }
}
