package com.tumugin.aisu.testing.app.graphql.query.groupQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetGroupQuery
import com.tumugin.aisu.testing.seeder.GroupSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetGroupTest : BaseKtorTest() {
  @Test
  fun testGetGroup() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val group = GroupSeeder().seedGroup(seededUserAndLoginInfo.user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetGroupQuery(GetGroupQuery.Variables(group.groupId.value.toString()))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.getGroup)
  }
}
