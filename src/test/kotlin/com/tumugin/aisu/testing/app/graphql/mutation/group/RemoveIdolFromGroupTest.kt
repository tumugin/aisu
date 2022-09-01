package com.tumugin.aisu.testing.app.graphql.mutation.group

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.RemoveIdolFromGroup
import com.tumugin.aisu.testing.seeder.GroupIdolSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RemoveIdolFromGroupTest : BaseKtorTest() {
  @Test
  fun testRemoveIdolFromGroup() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val group = GroupSeeder().seedGroup(seededUserAndLoginInfo.user.userId)
    val idol = IdolSeeder().seedIdol(seededUserAndLoginInfo.user.userId)
    GroupIdolSeeder().seedGroupIdol(group.groupId, idol.idolId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      RemoveIdolFromGroup(
        RemoveIdolFromGroup.Variables(
          groupId = group.groupId.value.toString(), idolId = idol.idolId.value.toString()
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.group?.removeIdolFromGroup)
  }
}
