package com.tumugin.aisu.testing.app.graphql.mutation.group

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddGroup
import com.tumugin.aisu.testing.graphql.client.enums.GroupStatus
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateGroupParamsInput
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddGroupTest : BaseKtorTest() {
  @Test
  fun testAddGroup() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddGroup(
        AddGroup.Variables(
          AddOrUpdateGroupParamsInput(
            groupName = "群青の世界", groupStatus = GroupStatus.PRIVATE_ACTIVE
          )
        )
      )
    ) {
      header("Cookie", seededUserAndLoginInfo.cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.group?.addGroup)
  }
}
