package com.tumugin.aisu.testing.app.graphql.query.idolQueryService

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetIdolQuery
import com.tumugin.aisu.testing.seeder.GroupIdolSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetIdolTest : BaseKtorTest() {
  @Test
  fun testGetIdol() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val user = seededUserAndLoginInfo.user
    val idol = IdolSeeder().seedIdol(user.userId)
    val group = GroupSeeder().seedGroup(user.userId)
    GroupIdolSeeder().seedGroupIdol(group.groupId, idol.idolId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetIdolQuery(GetIdolQuery.Variables(idol.idolId.value.toString()))
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data)
    Assertions.assertEquals("群青の世界", result.data!!.getIdol.groups.first().groupName)
  }
}
