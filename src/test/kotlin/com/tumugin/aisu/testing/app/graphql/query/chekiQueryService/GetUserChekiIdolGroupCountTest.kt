package com.tumugin.aisu.testing.app.graphql.query.chekiQueryService

import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetUserChekiIdolGroupCount
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetUserChekiIdolGroupCountTest : BaseKtorTest() {
  @Test
  fun testGetUserChekiIdolGroupCount() = testAisuApplication {
    val loggedUser = seedUserAndLoginAndGetCookieValue(this)
    val userId = loggedUser.user.userId
    val idol = IdolSeeder().seedIdol(userId)
    val firstGroup = GroupSeeder().seedGroup(userId)
    val secondGroup = GroupSeeder().seedGroup(userId)
    val firstRegulation = RegulationSeeder().seedRegulation(firstGroup.groupId, userId)
    val secondRegulation = RegulationSeeder().seedRegulation(secondGroup.groupId, userId)
    val chekiSeeder = ChekiSeeder()
    chekiSeeder.seedCheki(userId, idol.idolId, firstRegulation.regulationId)
    chekiSeeder.seedCheki(userId, idol.idolId, firstRegulation.regulationId, ChekiQuantity(2))
    chekiSeeder.seedCheki(userId, idol.idolId, secondRegulation.regulationId)
    chekiSeeder.seedCheki(userId, idol.idolId, regulationId = null, chekiQuantity = ChekiQuantity(2))

    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetUserChekiIdolGroupCount(
        GetUserChekiIdolGroupCount.Variables(
          "2021-01-01T00:00:00+09:00",
          "2021-12-31T23:59:59+09:00"
        )
      )
    ) {
      header("Cookie", loggedUser.cookieValue)
    }

    Assertions.assertNull(result.errors)
    val counts = requireNotNull(result.data?.currentUserChekis?.getUserChekiIdolGroupCount)
    Assertions.assertEquals(3, counts.size)
    val expectedIdolId = idol.idolId.value.toString()
    counts.forEach {
      Assertions.assertEquals(expectedIdolId, it.idolId)
      Assertions.assertEquals(expectedIdolId, it.idol?.idolId)
      Assertions.assertEquals(it.groupId, it.group?.groupId)
    }
    Assertions.assertNull(counts.single { it.groupId == null }.group)

    val firstGroupId = firstGroup.groupId.value.toString()
    val secondGroupId = secondGroup.groupId.value.toString()
    Assertions.assertEquals(
      mapOf(firstGroupId to 3, secondGroupId to 1, null to 2),
      counts.associate { it.groupId to it.chekiCount }
    )
    Assertions.assertEquals(
      mapOf(firstGroupId to 4_500, secondGroupId to 1_500, null to 0),
      counts.associate { it.groupId to it.totalPrice }
    )
  }
}
