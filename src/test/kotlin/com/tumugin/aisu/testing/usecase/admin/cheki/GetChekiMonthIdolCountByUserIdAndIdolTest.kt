package com.tumugin.aisu.testing.usecase.admin.cheki

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.admin.cheki.GetChekiAdmin
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetChekiMonthIdolCountByUserIdAndIdolTest : BaseDatabaseTest() {
  private val getChekiAdmin = GetChekiAdmin()
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
  fun testGetChekiMonthIdolCountByUserIdAndIdol() = runTest {
    ChekiSeeder().seedCheki(
      user.userId,
      idol.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00")),
      chekiQuantity = ChekiQuantity(1)
    )
    ChekiSeeder().seedCheki(
      user.userId,
      idolTwo.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2022-01-01T00:00:00+09:00")),
      chekiQuantity = ChekiQuantity(10)
    )

    val chekiMonthIdolCount =
      getChekiAdmin.getChekiMonthIdolCountByUserIdAndIdol(user.userId, TimeZone.of("Asia/Tokyo"))
    Assertions.assertEquals(
      listOf(
        ChekiMonthIdolCount(idol, idol.idolId, ChekiCount(1), ChekiShotAtMonth(2021, 12, TimeZone.of("Asia/Tokyo"))),
        ChekiMonthIdolCount(
          idolTwo,
          idolTwo.idolId,
          ChekiCount(10),
          ChekiShotAtMonth(2022, 1, TimeZone.of("Asia/Tokyo"))
        )
      ),
      chekiMonthIdolCount
    )
  }
}
