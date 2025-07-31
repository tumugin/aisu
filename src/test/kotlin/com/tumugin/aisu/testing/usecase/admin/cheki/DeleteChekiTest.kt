@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.tumugin.aisu.testing.usecase.admin.cheki

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.admin.cheki.GetChekiAdmin
import com.tumugin.aisu.usecase.admin.cheki.WriteChekiAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.time.ExperimentalTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteChekiTest : BaseDatabaseTest() {
  private val writeChekiAdmin = WriteChekiAdmin()
  private val getChekiAdmin = GetChekiAdmin()
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
      chekiShotAt = ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:00:00+09:00"))
    )
  }

  @Test
  fun testDeleteCheki() = runTest {
    writeChekiAdmin.deleteCheki(cheki.chekiId)
    Assertions.assertNull(getChekiAdmin.getCheki(cheki.chekiId))
  }

  @Test
  fun testDeleteChekiWithNoPermission() {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeChekiAdmin.deleteCheki(cheki.chekiId)
      }
    }
  }
}
