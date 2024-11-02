package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetUserAllChekis
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetUserAllChekisTest : BaseKtorTest() {
  @Test
  fun testGetUserAllChekis() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    for (i in 1..100) {
      ChekiSeeder().seedChekiWithAttachments(user.userId)
    }

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetUserAllChekis(GetUserAllChekis.Variables(1))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNotNull(result.data?.currentUserChekis?.getUserAllChekis?.chekis)

    // should has 100 chekis
    Assertions.assertEquals(100, result.data?.currentUserChekis?.getUserAllChekis?.count)

    // should be in page 1
    Assertions.assertEquals(1, result.data?.currentUserChekis?.getUserAllChekis?.currentPage)

    // should have 2 pages
    Assertions.assertEquals(2, result.data?.currentUserChekis?.getUserAllChekis?.pageCount)

    // should have 50 chekis in the first page
    Assertions.assertEquals(50, result.data?.currentUserChekis?.getUserAllChekis?.chekis?.size)

    Assertions.assertNull(result.errors)
  }
}
