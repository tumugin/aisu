package com.tumugin.aisu.testing.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.ChekiCount
import com.tumugin.aisu.domain.cheki.ChekiIdolCount
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.cheki.TotalPriceOfCheki
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetChekiIdolCountByUserIdTest : BaseDatabaseTest() {
  private val getCheki = GetCheki()
  private lateinit var user: User
  private lateinit var idol: Idol
  private lateinit var idolTwo: Idol
  private lateinit var regulation: Regulation
  private lateinit var group: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
    idolTwo = IdolSeeder().seedIdol(user.userId)
    group = GroupSeeder().seedGroup(user.userId)
    regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
  }

  @Test
  fun testGetChekiIdolCountByUserId() = runTest {
    ChekiSeeder().seedCheki(
      user.userId,
      idol.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
    )
    ChekiSeeder().seedCheki(
      user.userId,
      idolTwo.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2022-01-01T00:00:00+09:00"))
    )
    val chekiIdolCounts = getCheki.getChekiIdolCountByUserId(
      user.userId,
      ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00")),
      ChekiShotAt(Instant.parse("2022-01-01T00:00:00+09:00"))
    )
    Assertions.assertEquals(
      listOf(
        ChekiIdolCount(idol, idol.idolId, ChekiCount(1), TotalPriceOfCheki(1500)),
        ChekiIdolCount(idolTwo, idolTwo.idolId, ChekiCount(1), TotalPriceOfCheki(1500)),
      ),
      chekiIdolCounts
    )
  }
}
