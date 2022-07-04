package com.tumugin.aisu.testing.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import com.tumugin.aisu.usecase.client.cheki.WriteCheki
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteChekiTest : BaseDatabaseTest() {
  private val writeCheki = WriteCheki()
  private val getCheki = GetCheki()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var idol: Idol
  private lateinit var cheki: Cheki

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
    cheki = ChekiSeeder().seedCheki(
      user.userId,
      idol.idolId,
      RegulationSeeder().seedRegulation(GroupSeeder().seedGroup(user.userId).groupId, user.userId).regulationId,
      chekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:00:00+09:00"))
    )
  }

  @Test
  fun testDeleteCheki() = runTest {
    writeCheki.deleteCheki(user.userId, cheki.chekiId)
    Assertions.assertNull(getCheki.getCheki(user.userId, cheki.chekiId))
  }

  @Test
  fun testDeleteChekiWithNoPermission() {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeCheki.deleteCheki(userTwo.userId, cheki.chekiId)
      }
    }
  }
}
