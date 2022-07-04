package com.tumugin.aisu.testing.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.client.cheki.WriteCheki
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateChekiTest : BaseDatabaseTest() {
  private val writeCheki = WriteCheki()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var idol: Idol
  private lateinit var idolTwo: Idol
  private lateinit var regulation: Regulation
  private lateinit var regulationTwo: Regulation
  private lateinit var cheki: Cheki
  private lateinit var chekiTwo: Cheki

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
    idolTwo = IdolSeeder().seedIdol(userTwo.userId)
    regulation = RegulationSeeder().seedRegulation(GroupSeeder().seedGroup(user.userId).groupId, user.userId)
    regulationTwo = RegulationSeeder().seedRegulation(GroupSeeder().seedGroup(userTwo.userId).groupId, user.userId)
    cheki = ChekiSeeder().seedCheki(
      user.userId,
      idol.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
    )
    chekiTwo = ChekiSeeder().seedCheki(
      userTwo.userId,
      idol.idolId,
      regulation.regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
    )
  }

  @Test
  fun testUpdateCheki() = runTest {
    val newIdol = IdolSeeder().seedIdol(user.userId)
    val newRegulation = RegulationSeeder().seedRegulation(GroupSeeder().seedGroup(user.userId).groupId, user.userId)
    val updatedCheki = writeCheki.updateCheki(
      cheki.chekiId,
      user.userId,
      newIdol.idolId,
      newRegulation.regulationId,
      ChekiQuantity(100),
      ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00"))
    )

    Assertions.assertEquals(user.userId, updatedCheki.userId)
    Assertions.assertEquals(newIdol.idolId, updatedCheki.idolId)
    Assertions.assertEquals(newRegulation.regulationId, updatedCheki.regulationId)
    Assertions.assertEquals(ChekiQuantity(100), updatedCheki.chekiQuantity)
    Assertions.assertEquals(ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00")), updatedCheki.chekiShotAt)
  }

  @Test
  fun testUpdateChekiWithNoPermission() {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeCheki.updateCheki(
          chekiTwo.chekiId,
          user.userId,
          idol.idolId,
          regulation.regulationId,
          ChekiQuantity(100),
          ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00"))
        )
      }
    }
  }

  @Test
  fun testUpdateChekiWithNotExistIdol() {
    Assertions.assertThrows(InvalidContextException::class.java) {
      runBlocking {
        writeCheki.updateCheki(
          cheki.chekiId,
          user.userId,
          IdolId(1000),
          regulation.regulationId,
          ChekiQuantity(100),
          ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00"))
        )
      }
    }
  }

  @Test
  fun testUpdateChekiWithNotVisibleIdol() {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeCheki.updateCheki(
          cheki.chekiId,
          user.userId,
          idolTwo.idolId,
          regulation.regulationId,
          ChekiQuantity(100),
          ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00"))
        )
      }
    }
  }

  @Test
  fun testUpdateChekiWithNotVisibleRegulation() {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeCheki.updateCheki(
          cheki.chekiId,
          user.userId,
          idol.idolId,
          regulationTwo.regulationId,
          ChekiQuantity(100),
          ChekiShotAt(Instant.parse("2021-12-02T00:00:00+09:00"))
        )
      }
    }
  }
}
