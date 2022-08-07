package com.tumugin.aisu.testing.app.graphql.mutation.chekiMutation

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddCheki
import com.tumugin.aisu.testing.graphql.client.inputs.AddOrUpdateChekiParamsInput
import com.tumugin.aisu.testing.seeder.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddChekiTest : BaseKtorTest() {
  private val userSeeder = UserSeeder()
  private val idolSeeder = IdolSeeder()
  private val regulationSeeder = RegulationSeeder()
  private val groupIdolSeeder = GroupIdolSeeder()
  private val groupSeeder = GroupSeeder()
  lateinit var user: User
  lateinit var idol: Idol
  lateinit var regulation: Regulation
  lateinit var group: Group
  lateinit var cookieValue: String

  @BeforeEach
  fun beforeEach() = testAisuApplication {
    val seededUserAndLoginInfo = seedUserAndLoginAndGetCookieValue(this)
    user = seededUserAndLoginInfo.user
    cookieValue = seededUserAndLoginInfo.cookieValue
    idol = idolSeeder.seedIdol(user.userId)
    group = groupSeeder.seedGroup(user.userId)
    groupIdolSeeder.seedGroupIdol(group.groupId, idol.idolId)
    regulation = regulationSeeder.seedRegulation(group.groupId, user.userId)
  }

  @Test
  fun testAddCheki() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddCheki(
        AddCheki.Variables(
          AddOrUpdateChekiParamsInput(
            chekiQuantity = 5,
            chekiShotAt = "2022-12-07T15:30:00+09:00",
            idolId = idol.idolId.value.toString(),
            regulationId = regulation.regulationId.value.toString()
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }
    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.cheki?.addCheki)
  }
}
