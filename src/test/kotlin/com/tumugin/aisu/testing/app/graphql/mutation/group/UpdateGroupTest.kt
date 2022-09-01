package com.tumugin.aisu.testing.app.graphql.mutation.group

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UpdateGroup
import com.tumugin.aisu.testing.graphql.client.enums.GroupStatus
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateGroupParamsInput
import com.tumugin.aisu.testing.seeder.GroupSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UpdateGroupTest : BaseKtorTest() {
  @Test
  fun updateGroup() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    val group = GroupSeeder().seedGroup(seededUserAndLoginInfo.user.userId)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      UpdateGroup(
        UpdateGroup.Variables(
          group.groupId.value.toString(),
          AddOrUpdateGroupParamsInput(
            groupName = "Appare!", groupStatus = GroupStatus.PRIVATE_ACTIVE
          )
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.group?.updateGroup)
  }
}
