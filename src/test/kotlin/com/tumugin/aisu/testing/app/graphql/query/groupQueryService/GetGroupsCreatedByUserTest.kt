package com.tumugin.aisu.testing.app.graphql.query.groupQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetGroupsCreatedByUserQuery
import com.tumugin.aisu.testing.seeder.GroupSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetGroupsCreatedByUserTest : BaseKtorTest() {
  @Test
  fun testGetGroupsCreatedByUser() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    GroupSeeder().seedGroup(seededUserAndLoginInfo.user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetGroupsCreatedByUserQuery(GetGroupsCreatedByUserQuery.Variables(1))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.currentUserGroups?.getGroupsCreatedByUser?.groups)
    Assertions.assertEquals(1, result.data?.currentUserGroups?.getGroupsCreatedByUser?.pageCount)
    Assertions.assertEquals(1, result.data?.currentUserGroups?.getGroupsCreatedByUser?.groups?.size)
  }
}
