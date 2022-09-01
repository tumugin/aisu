package com.tumugin.aisu.testing.app.graphql.mutation.group

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddIdolToGroup
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddIdolToGroupTest : BaseKtorTest() {
  @Test
  fun testAddIdolToGroup() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val group = GroupSeeder().seedGroup(seededUserAndLoginInfo.user.userId)
    val idol = IdolSeeder().seedIdol(seededUserAndLoginInfo.user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddIdolToGroup(
        AddIdolToGroup.Variables(
          groupId = group.groupId.value.toString(), idolId = idol.idolId.value.toString()
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.group?.addIdolToGroup)
  }
}
