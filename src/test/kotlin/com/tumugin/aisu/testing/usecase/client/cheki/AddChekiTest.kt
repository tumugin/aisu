package com.tumugin.aisu.testing.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.cheki.WriteCheki
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddChekiTest : BaseDatabaseTest() {
  private val writeCheki = WriteCheki()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var idol: Idol
  private lateinit var idolTwo: Idol
  private lateinit var regulation: Regulation
  private lateinit var group: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
    idolTwo = IdolSeeder().seedIdol(userTwo.userId)
    group = GroupSeeder().seedGroup(user.userId)
    regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
  }

  @Test
  fun testAddCheki() = runTest {
    val cheki = writeCheki.addCheki(
      user.userId,
      idol.idolId,
      regulation.regulationId,
      ChekiQuantity(10),
      ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
    )
    Assertions.assertEquals(user.userId, cheki.userId)
    Assertions.assertEquals(idol.idolId, cheki.idolId)
    Assertions.assertEquals(regulation.regulationId, cheki.regulationId)
    Assertions.assertEquals(ChekiQuantity(10), cheki.chekiQuantity)
    Assertions.assertEquals(ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00")), cheki.chekiShotAt)
  }

  @Test
  fun testAddChekiWithNotExistIdol() {
    Assertions.assertThrows(InvalidContextException::class.java) {
      runBlocking {
        writeCheki.addCheki(
          user.userId,
          IdolId(10000),
          regulation.regulationId,
          ChekiQuantity(10),
          ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
        )
      }
    }
  }

  @Test
  fun testAddChekiWithNotVisibleIdol() {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeCheki.addCheki(
          user.userId,
          idolTwo.idolId,
          regulation.regulationId,
          ChekiQuantity(10),
          ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
        )
      }
    }
  }
}
