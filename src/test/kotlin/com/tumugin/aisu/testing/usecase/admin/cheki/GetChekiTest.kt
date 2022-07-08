package com.tumugin.aisu.testing.usecase.admin.cheki

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import com.tumugin.aisu.usecase.admin.cheki.GetChekiAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetChekiTest : BaseDatabaseTest() {
  private val getChekiAdmin = GetChekiAdmin()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var cheki: Cheki

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    val idol = IdolSeeder().seedIdol(user.userId)
    val group = GroupSeeder().seedGroup(user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    cheki = ChekiSeeder().seedCheki(user.userId, idol.idolId, regulation.regulationId)
  }

  @Test
  fun getChekiTest() = runTest {
    val retrievedCheki = getChekiAdmin.getCheki(cheki.chekiId)
    Assertions.assertEquals(cheki, retrievedCheki)
  }

  @Test
  fun getChekiWithNoPermissionTest() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        getChekiAdmin.getCheki(cheki.chekiId)
      }
    }
  }
}
