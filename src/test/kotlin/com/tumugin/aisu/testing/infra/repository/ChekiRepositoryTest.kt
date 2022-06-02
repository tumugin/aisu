package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.cheki.ChekiRepository
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.*
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ChekiRepositoryTest : BaseDatabaseTest() {
  private val chekiRepository by inject<ChekiRepository>()

  @BeforeTest
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    val group = GroupSeeder().seedGroup(user.userId)
    val idol = IdolSeeder().seedIdol(group.groupId, user.userId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
    (1..10).forEach { _ ->
      ChekiSeeder().seedCheki(user.userId, idol.idolId, regulation.regulationId)
    }
  }

  @Test
  fun testGetCheki() = runTest {
    val cheki = chekiRepository.getCheki(ChekiId(1))
    assertNotNull(cheki)
  }
}
