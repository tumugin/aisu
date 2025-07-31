@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiRepository
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import kotlinx.coroutines.test.runTest
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class ChekiRepositoryTest : BaseDatabaseTest() {
  private val chekiRepository by inject<ChekiRepository>()

  @BeforeEach
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    val group = GroupSeeder().seedGroup(user.userId)
    val idolUna = IdolSeeder().seedIdol(
      user.userId,
      IdolName("村崎ゆうな")
    )
    val idolMika = IdolSeeder().seedIdol(
      user.userId,
      IdolName("工藤みか")
    )
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    // 2021/12/01の0時(JST)ぴったりに10枚のチェキを10回登録
    (1..10).forEach { _ ->
      ChekiSeeder().seedCheki(
        user.userId,
        idolUna.idolId,
        regulation.regulationId,
        ChekiQuantity(10),
        ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:00:00+09:00"))
      )
    }
    // 2021/12/31の23:59:59(JST)ぴったりに10枚のチェキを10回登録
    (1..10).forEach { _ ->
      ChekiSeeder().seedCheki(
        user.userId,
        idolMika.idolId,
        regulation.regulationId,
        ChekiQuantity(10),
        ChekiShotAt(kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00"))
      )
    }
    // 2022/01/01の00:00:00(JST)ぴったりに10枚のチェキを10回登録
    (1..10).forEach { _ ->
      ChekiSeeder().seedCheki(
        user.userId,
        idolMika.idolId,
        regulation.regulationId,
        ChekiQuantity(10),
        ChekiShotAt(kotlin.time.Instant.parse("2022-01-01T00:00:00+09:00"))
      )
    }
  }

  @Test
  fun testGetCheki() = runTest {
    val cheki = chekiRepository.getCheki(ChekiId(1))
    assertNotNull(cheki)
  }

  @Test
  fun testGetChekiByUserIdAndShotDateTimeRange() = runTest {
    val chekis = chekiRepository.getChekiByUserIdAndShotDateTimeRange(
      UserId(1),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:00:00+09:00")),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00"))
    )
    assertEquals(20, chekis.size)
  }

  @Test
  fun testGetChekiByUserIdAndShotDateTimeRangeAndIdolId() = runTest {
    val chekis = chekiRepository.getChekiByUserIdAndShotDateTimeRangeAndIdolId(
      UserId(1),
      IdolId(1),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:00:00+09:00")),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00"))
    )
    assertEquals(10, chekis.size)
  }

  @Test
  fun testGetChekiIdolCountByUserId() = runTest {
    val chekiIdolCounts = chekiRepository.getChekiIdolCountByUserId(
      UserId(1),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:00:00+09:00")),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00"))
    )
    assertEquals(2, chekiIdolCounts.size)
    assertNotNull(chekiIdolCounts[0].idol)
    assertEquals(100, chekiIdolCounts[0].chekiCount.value)
    assertNotNull(chekiIdolCounts[1].idol)
    assertEquals(100, chekiIdolCounts[1].chekiCount.value)
  }

  @Test
  fun testGetChekiMonthIdolCountByUserIdAndIdol() = runTest {
    val chekiMonthIdolCount = chekiRepository.getChekiMonthIdolCountByUserIdAndIdol(
        UserId(1),
        TimeZone.of("Asia/Tokyo")
    )
    assertEquals(3, chekiMonthIdolCount.size)
    assertNotNull(chekiMonthIdolCount[0].idol)
    assertNotNull(chekiMonthIdolCount[1].idol)
    assertNotNull(chekiMonthIdolCount[2].idol)
  }

  @Test
  fun testAddCheki() = runTest {
    val cheki = chekiRepository.addCheki(
      UserId(1),
      IdolId(1),
      RegulationId(1),
      ChekiQuantity(10),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00"))
    )
    val addedResult = chekiRepository.getCheki(cheki.chekiId)!!
    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 1L,
        "idolId" to 1L,
        "regulationId" to 1L,
        "chekiQuantity" to 10,
        "chekiShotAt" to kotlin.time.Instant.parse("2021-12-31T23:59:59+09:00").toString()
      ), mutableMapOf<String, Any?>(
        "userId" to addedResult.userId.value,
        "idolId" to addedResult.idolId?.value,
        "regulationId" to addedResult.regulation?.regulationId?.value,
        "chekiQuantity" to addedResult.chekiQuantity.value,
        "chekiShotAt" to addedResult.chekiShotAt.value.toString()
      )
    )
  }

  @Test
  fun testUpdateCheki() = runTest {
    val cheki = chekiRepository.updateCheki(
      ChekiId(1),
      UserId(1),
      IdolId(1),
      RegulationId(1),
      ChekiQuantity(99),
      ChekiShotAt(kotlin.time.Instant.parse("2021-12-15T20:10:30+09:00"))
    )
    val addedResult = chekiRepository.getCheki(cheki.chekiId)!!
    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 1L,
        "idolId" to 1L,
        "regulationId" to 1L,
        "chekiQuantity" to 99,
        "chekiShotAt" to kotlin.time.Instant.parse("2021-12-15T20:10:30+09:00").toString()
      ), mutableMapOf<String, Any?>(
        "userId" to addedResult.userId.value,
        "idolId" to addedResult.idolId?.value,
        "regulationId" to addedResult.regulation?.regulationId?.value,
        "chekiQuantity" to addedResult.chekiQuantity.value,
        "chekiShotAt" to addedResult.chekiShotAt.value.toString()
      )
    )
  }

  @Test
  fun testDeleteCheki() = runTest {
    assertNotNull(chekiRepository.getCheki(ChekiId(1)))
    chekiRepository.deleteCheki(ChekiId(1))
    assertNull(chekiRepository.getCheki(ChekiId(1)))
  }
}
