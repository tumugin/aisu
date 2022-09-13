package com.tumugin.aisu.testing.app.graphql.mutation.favoriteGroup

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.DeleteFavoriteGroup
import com.tumugin.aisu.testing.seeder.FavoriteGroupSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteFavoriteGroupTest : BaseKtorTest() {
  lateinit var user: User
  lateinit var cookieValue: String

  @BeforeEach
  fun beforeEach() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    user = seededUserAndLoginInfo.user
    cookieValue = seededUserAndLoginInfo.cookieValue
  }

  @Test
  fun testDeleteFavoriteGroup() = testAisuApplication {
    val group = GroupSeeder().seedGroup(user.userId)
    val favoriteGroup = FavoriteGroupSeeder().seedFavoriteGroup(user.userId, group.groupId)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      DeleteFavoriteGroup(
        DeleteFavoriteGroup.Variables(
          favoriteGroup.favoriteGroupId.value.toString()
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.favoriteGroup?.deleteFavoriteGroup)
  }
}
